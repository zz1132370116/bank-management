package com.zl.dc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.zl.dc.config.*;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.service.UserService;
import com.zl.dc.util.BankUserPasswordUtil;
import com.zl.dc.util.NumberValid;
import com.zl.dc.util.StarUtil;
import com.zl.dc.util.StringValid;
import com.zl.dc.vo.BankUserVo;
import com.zl.dc.vo.BaseResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @version: V1.0
 * @author: zhanglei
 * @className: UserController
 * @description: 用户信息的处理controller
 * @data: 2019/8/5 17:04
 */
@RequestMapping
@RestController
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private StringRedisTemplate redisTemplate;

    /**
     * @author pds
     * @param phone
     * @return org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description 注册----获取验证码
     * @date 2019/8/13 16:47
     */
    @GetMapping("/registrySms/{phone}")
    public ResponseEntity<BaseResult> registrySms(@PathVariable("phone") String phone){
        if (StringUtils.isNotBlank(phone)){
            if (!NumberValid.verifyPhone(phone)){
                return ResponseEntity.ok(new BaseResult(1,"手机号不正确"));
            }
            String code = RandomStringUtils.randomNumeric(6);

            SendSmsResponse smsResponse;
            try {
                //3、发送短信
                smsResponse = SmsRegistry.sendSms(phone, code);
            }catch (ClientException e){
                e.printStackTrace();
                return ResponseEntity.ok(new BaseResult(1, "发送失败"));
            }
            String smsStatus = "OK";
            if (smsStatus.equalsIgnoreCase(smsResponse.getCode())) {
                redisTemplate.opsForValue().set(phone+code,code,5,TimeUnit.MINUTES);
                return ResponseEntity.ok(new BaseResult(0, "发送成功"));
            } else {
                return ResponseEntity.ok(new BaseResult(1, smsResponse.getMessage()));
            }
        }

        return ResponseEntity.ok(new BaseResult(1,"手机号不能为空"));
    }

    /**
     * @author pds
     * @param bankUserVo
     * @return com.zl.dc.vo.BaseResult
     * @description 注册----注册
     * @date 2019/8/13 16:47
     */
    @PostMapping("/register")
    public BaseResult register(@RequestBody BankUserVo bankUserVo){
        if(StringValid.isBlank(bankUserVo.getUserPhone(),bankUserVo.getCode())){
            //通过手机号+验证码从redis中获取验证码
            String code = redisTemplate.opsForValue().get(bankUserVo.getUserPhone()+bankUserVo.getCode());
            //判断传过来的验证码是否正确
            if (code != null){
                //通过手机号查询用户信息
                BankUser bankUserByUserPhone = userService.getBankUserByUserPhone(bankUserVo.getUserPhone());
                if(bankUserByUserPhone != null){
                    userService.updateBankUserPhoneToNull(bankUserByUserPhone);
                }
                BankUser bankUser = new BankUser();
                bankUser.setUserPhone(bankUserVo.getUserPhone());
                bankUser.setUserPassword(BankUserPasswordUtil.generate(bankUserVo.getUserPassword()));
                bankUser.setUserName("");
                Byte status = 100;
                bankUser.setUserStatus(status);
                bankUser.setDefaultBankCard("");
                bankUser.setGmtCreate(new Date());
                bankUser.setGmtModified(new Date());
                userService.addBankUser(bankUser);
                return new BaseResult(0,"注册成功");
            }
            return new BaseResult(1,"对不起，验证码错误或已过期");
        }
        return new BaseResult(1,"注册失败");
    }

    /**
     * @author pds
     * @param bankUser
     * @return com.zl.dc.vo.BaseResult
     * @description 通过手机号或身份证号登录
     * @date 2019/8/12 19:59
     */
    @PostMapping("/query")
    public BaseResult queryUser(@RequestBody BankUserVo bankUser) {
        BankUser user;

        if (!StringUtils.isNotBlank(bankUser.getUserPhone())) {
            //通过身份证号查询用户信息
            user = this.userService.getBankUserByIdCard(bankUser.getIdCard());
        } else {
            //通过手机号查询用户信息
            user = this.userService.getBankUserByUserPhone(bankUser.getUserPhone());
        }
        //非空判断
        if (user != null) {
            //密码错误次数达到3次之后会暂时将账号冻结，冻结今天的剩余时间，即在过了24:00之后就可以登录，在冻结时间之内不允许该用户登录
            // 这里先从redis里查该用户是否被禁止登录
            String timesStr = redisTemplate.opsForValue().get(user.getUserPhone()+user.getIdCard());
            if (timesStr != null && timesStr.equals("3")){
                return new BaseResult(3,"您今天的输错密码的次数为3次，请明天再试，或者选择忘记密码");
            }

            //校验密码
            boolean verifyPassword = BankUserPasswordUtil.verify(bankUser.getUserPassword(), user.getUserPassword());
            //密码正确
            if (verifyPassword) {
                //登录成功，移除登录失败次数
                redisTemplate.delete(user.getUserPhone()+user.getIdCard());
                redisTemplate.opsForValue().set("user-"+user.getUserId().toString()+"-userInfo", JSON.toJSONString(user));
                redisTemplate.opsForValue().set(user.getUserPhone(),JSONObject.toJSONString(user));
                redisTemplate.opsForValue().set(user.getUserId().toString(),JSONObject.toJSONString(user));
                return new BaseResult(0,"登录成功").append("user",user);
            }
            else{
                Integer times = RedisInsertUtil.addingData(redisTemplate, user.getUserPhone() + user.getIdCard(), timesStr);
                return new BaseResult(2,"输错密码"+times+"次，只有"+(3-times)+"次机会了");
            }
        }
        //3 失败
        return new BaseResult(1,"对不起，没有这个账号");
    }

    /**
     * @author pds
     * @param bankUser
     * @return com.zl.dc.vo.BaseResult
     * @description 根据验证码登录
     * @date 2019/8/12 19:59
     */
    @PostMapping("/loginBySendSms")
    public BaseResult loginBySendSms(@RequestBody BankUserVo bankUser) {
        String code = redisTemplate.opsForValue().get(bankUser.getUserPhone()+bankUser.getCode());
        //判断传过来的验证码是否正确
        if (code != null){
            BankUser bankUserByUserPhone = userService.getBankUserByUserPhone(bankUser.getUserPhone());
            redisTemplate.delete(bankUser.getUserPhone()+bankUser.getCode());
            redisTemplate.opsForValue().set("user-"+bankUserByUserPhone.getUserId().toString()+"-userInfo", JSON.toJSONString(bankUserByUserPhone));
            redisTemplate.opsForValue().set(bankUserByUserPhone.getUserPhone(),JSONObject.toJSONString(bankUserByUserPhone));
            redisTemplate.opsForValue().set(bankUserByUserPhone.getUserId().toString(),JSONObject.toJSONString(bankUserByUserPhone));
            return new BaseResult(0,"登录成功").append("user",bankUserByUserPhone);
        }else{
            return new BaseResult(1,"对不起，验证码错误或已过期");
        }
    }

    /**
     * @author pds
     * @param bankUser
     * @return org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description 通过验证码登录----获取验证码
     * @date 2019/8/21 20:03
     */
    @PostMapping("/sendSms")
    public ResponseEntity<BaseResult> sendSms(@RequestBody BankUser bankUser) {

        try {
            if (StringUtils.isNotBlank(bankUser.getUserPhone())) {
                //验证手机号是否正确
                if (!NumberValid.verifyPhone(bankUser.getUserPhone())){
                    return ResponseEntity.ok(new BaseResult(1, "该手机号不正确"));
                }

                //先查询是否存在此账号
                BankUser user = userService.getBankUserByUserPhone(bankUser.getUserPhone());

                if(user == null){
                    return ResponseEntity.ok(new BaseResult(1, "对不起，没有这个账号"));
                }

                //密码错误次数达到3次之后会暂时将账号冻结，在冻结时间之内不允许该用户登录
                // 这里先从redis里查该用户是否被禁止登录
                String timesStr = redisTemplate.opsForValue().get(user.getUserPhone()+user.getIdCard());
                if (StringUtils.isNotBlank(timesStr) && timesStr.equals("3")){
                    return ResponseEntity.ok(new BaseResult(3,"您今天的输错密码的次数为3次，请明天再试，或者选择忘记密码"));
                }

                //发送短信
                //1 生产验证码
                String code = RandomStringUtils.randomNumeric(6);
                //存redis5分钟，因为用户的信息是使用手机号为key存放到redis中的，为了防止将用户信息覆盖，
                // 因此将这里的key设置为手机号+验证码
                redisTemplate.opsForValue().set(bankUser.getUserPhone()+code, code, 5, TimeUnit.MINUTES);
                //3 发送短信
                SendSmsResponse smsResponse = SmsLogin.sendSms(bankUser.getUserPhone(), code);
                if ("OK".equalsIgnoreCase(smsResponse.getCode())) {
                    return ResponseEntity.ok(new BaseResult(0, "发送成功"));
                } else {
                    return ResponseEntity.ok(new BaseResult(0, smsResponse.getMessage()));
                }
            } else {
                return ResponseEntity.ok(new BaseResult(1, "请稍后再点"));
            }
        } catch (ClientException e) {
            e.printStackTrace();
            return ResponseEntity.ok(new BaseResult(1, "发送失败"));
        }
    }

    /**
     * @author: pds
     * @params:  [user]
     * @return: org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 修改密码----获取验证码
     * @data: 2019/8/7 16:28
     */
    @PostMapping("/updatePasswordSms")
    public ResponseEntity<BaseResult> updatePasswordSms(@RequestBody BankUserVo user){
        //判断手机号是否为空
        if (StringUtils.isNotBlank(user.getUserPhone())){
            //验证手机号是否正确
            if (!NumberValid.verifyPhone(user.getUserPhone())){
                return ResponseEntity.ok(new BaseResult(1, "该手机号不正确"));
            }

            //从数据库中根据手机号获取对应的用户信息
            BankUser bankUser = userService.getBankUserByUserPhone(user.getUserPhone());
            //如果不存在此用户
            if (bankUser == null){
                return ResponseEntity.ok(new BaseResult(1, "该手机未注册账号，请重新输入"));
            }

            //发送短信
            //1、生成验证码
            String code = RandomStringUtils.randomNumeric(6);
            //存redis5分钟，因为用户的信息是使用手机号为key存放到redis中的，为了防止将用户信息覆盖，
            // 因此将这里的key设置为手机号+验证码
            redisTemplate.opsForValue().set(user.getUserPhone()+code,code,5,TimeUnit.MINUTES);
            SendSmsResponse smsResponse;
            try {
                //3、发送短信
                smsResponse = SmsChangePassword.sendSms(user.getUserPhone(), code);
            }catch (ClientException e){
                e.printStackTrace();
                return ResponseEntity.ok(new BaseResult(1, "发送失败"));
            }
            String smsStatus = "OK";
            //判断发送手机验证码的返回值的状态是否是OK，如果是则发送成功，否则发送失败
            if (smsStatus.equalsIgnoreCase(smsResponse.getCode())) {
                return ResponseEntity.ok(new BaseResult(0, "发送成功"));
            } else {
                return ResponseEntity.ok(new BaseResult(1, smsResponse.getMessage()));
            }
        }
        return ResponseEntity.ok(new BaseResult(1, "请稍后再点"));
    }

    /**
     * @author pds
     * @param user
     * @return org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description 修改密码----验证验证码是否正确
     * @date 2019/8/11 20:24
     */
    @PostMapping("/updatePasswordVerify")
    public ResponseEntity<BaseResult> updatePasswordVerify(@RequestBody BankUserVo user){
        if (StringValid.isBlank(user.getUserPhone(),user.getCode())){
            //验证手机号是否正确
            if (!NumberValid.verifyPhone(user.getUserPhone())){
                return ResponseEntity.ok(new BaseResult(1, "该手机号不正确"));
            }

            //通过手机号+验证码从redis中获取验证码
            String code = redisTemplate.opsForValue().get(user.getUserPhone()+user.getCode());
            //判断传过来的验证码和从redis中获取的验证码是否一致
            if(code != null && !"".equals(code)){
                redisTemplate.delete(user.getUserPhone()+user.getCode());
                return ResponseEntity.ok(new BaseResult(0, "验证码正确"));
            }
            return ResponseEntity.ok(new BaseResult(1, "验证码错误或验证码已经过时"));
        }
        return ResponseEntity.ok(new BaseResult(1, "验证码错误或验证码已经过时"));
    }

    /**
     * @author pds
     * @param user
     * @return org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description 修改密码----修改密码
     * @date 2019/8/11 20:25
     */
    @PostMapping("/updateBankUserPassword")
    public ResponseEntity<BaseResult> updateBankUserPassword(@RequestBody BankUserVo user){
        if (StringValid.isBlank(user.getUserPhone(),user.getUserPassword(),user.getPasswordConfig())){
            //验证手机号是否正确
            if (!NumberValid.verifyPhone(user.getUserPhone())){
                return ResponseEntity.ok(new BaseResult(1, "该手机号不正确"));
            }

            //判断新密码和确认新密码是否相等
            boolean equal = user.getUserPassword().equals(user.getPasswordConfig());
            //如果相等则执行下面的操作
            if (equal){
                BankUser bankUser1 = new BankUser();
                bankUser1.setUserPhone(user.getUserPhone());
                //将密码加密
                bankUser1.setUserPassword(BankUserPasswordUtil.generate(user.getUserPassword()));
                //执行修改密码操作
                BankUser bankUser = userService.updateBankUserPassword(bankUser1);
                redisTemplate.delete(bankUser.getUserPhone()+bankUser.getIdCard());
                //将修改手机号之后的用户的信息保存到redis中，使用手机号作为key
                redisTemplate.opsForValue().set("user-"+user.getUserId().toString()+"-userInfo", JSON.toJSONString(user));
                redisTemplate.opsForValue().set(bankUser.getUserPhone(),JSONObject.toJSONString(bankUser));
                //将修改手机号之后的用户的信息保存到redis中，使用用户id作为key
                redisTemplate.opsForValue().set(bankUser.getUserId().toString(),JSONObject.toJSONString(bankUser));
                //返回信息给用户
                return ResponseEntity.ok(new BaseResult(0, "密码修改成功"));
            }else {
                return ResponseEntity.ok(new BaseResult(1, "密码修改失败"));
            }
        }else {
            return ResponseEntity.ok(new BaseResult(1, "密码修改失败"));
        }
    }

    /**
     * @author pds
     * @param user
     * @return org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description 修改手机----获取验证码
     * @date 2019/8/11 20:25
     */
    @PostMapping("/updatePhoneSms")
    public ResponseEntity<BaseResult> updatePhoneSms(@RequestBody BankUserVo user){
        boolean isOldPhone = StringUtils.isNotBlank(user.getOldPhone());
        if (isOldPhone){
            //验证旧手机号是否正确
            if (!NumberValid.verifyPhone(user.getOldPhone())){
                return ResponseEntity.ok(new BaseResult(1, "该手机号不正确"));
            }
        }
        boolean isUserPhone = StringUtils.isNotBlank(user.getUserPhone());
        if (isUserPhone){
            //验证新手机号是否正确
            if (!NumberValid.verifyPhone(user.getUserPhone())){
                return ResponseEntity.ok(new BaseResult(1, "该手机号不正确"));
            }
        }

        //判断手机号是否为空，旧手机号或新手机号
        if (isOldPhone || isUserPhone){
            //如果oldPhone不为空，而userPhone为空，那么就是第一次获取验证码，此时的oldPhone是数据库里存在的，
            // 是这个账号当前绑定的，因此需要判断此时输入的手机号是否是当前账号绑定的。
            if(isOldPhone && !isUserPhone){
                //从redis中根据手机号获取对应的用户信息
                String bankUserStr = redisTemplate.opsForValue().get(user.getOldPhone());
                if (!StringUtils.isNotBlank(bankUserStr)){
                    return ResponseEntity.ok(new BaseResult(1, "该手机未注册账号，请重新输入"));
                }

                this.updatePhoneSms(user.getOldPhone());
            }
            //如果oldPhone不为空，且userPhone不为空，那么就是第二次获取验证码，此时的userPhone是数据库里不存在的，
            // 是这个账号将要绑定的，因此不需要判断此时的手机号是否存在
            else if(isOldPhone && isUserPhone){
                if (user.getOldPhone().equals(user.getUserPhone())){
                    return ResponseEntity.ok(new BaseResult(1, "新手机号与原手机号不能相同"));
                }
                this.updatePhoneSms(user.getUserPhone());
            }
        }
        return ResponseEntity.ok(new BaseResult(0, "请稍后再点"));
    }

    /**
     * @author pds
     * @param phone
     * @return org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description 由于修改手机号要获取两次验证码，因此将获取验证码这部分代码提取出来
     * @date 2019/8/11 20:26
     */
    public ResponseEntity<BaseResult> updatePhoneSms(String phone){
        //发送短信
        //1、生成验证码
        String code = RandomStringUtils.randomNumeric(6);
        //存redis5分钟，因为用户的信息是使用手机号为key存放到redis中的，为了防止将用户信息覆盖，
        // 因此将这里的key设置为手机号+验证码
        redisTemplate.opsForValue().set(phone+code,code,5,TimeUnit.MINUTES);
        //3、发送短信
        SendSmsResponse smsResponse;
        try {
            //3、发送短信
            smsResponse = SmsChangePhone.sendSms(phone, code);
        }catch (ClientException e){
            e.printStackTrace();
            return ResponseEntity.ok(new BaseResult(1, "发送失败"));
        }
        String smsStatus = "OK";
        //判断发送手机验证码的返回值的状态是否是OK，如果是则发送成功，否则发送失败
        if (smsStatus.equalsIgnoreCase(smsResponse.getCode())) {
            return ResponseEntity.ok(new BaseResult(0, "发送成功"));
        } else {
            return ResponseEntity.ok(new BaseResult(1, smsResponse.getMessage()));
        }
    }

    /**
     * @author pds
     * @param user
     * @return org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description 修改手机----验证验证码
     * @date 2019/8/11 20:26
     */
    @PostMapping("/updatePhoneVerify")
    public ResponseEntity<BaseResult> updatePhoneVerify(@RequestBody BankUserVo user){
        if (StringValid.isBlank(user.getOldPhone(),user.getCode())){
            //验证手机号是否正确
            if (!NumberValid.verifyPhone(user.getOldPhone())){
                return ResponseEntity.ok(new BaseResult(1, "该手机号不正确"));
            }
            //通过手机号+验证码从redis中获取验证码
            String code = redisTemplate.opsForValue().get(user.getOldPhone()+user.getCode());
            //判断传过来的验证码和从redis中获取的验证码是否一致
            if(code != null && !"".equals(code)){
                redisTemplate.delete(user.getOldPhone()+user.getCode());
                return ResponseEntity.ok(new BaseResult(0, "验证码正确"));
            }
            return ResponseEntity.ok(new BaseResult(1, "验证码错误或验证码已经过时"));
        }
        return ResponseEntity.ok(new BaseResult(0, "验证码错误或验证码已经过时"));
    }

    /**
     * @author pds
     * @param bankUser
     * @return org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description 修改手机
     * @date 2019/8/11 20:26
     */
    @PostMapping("/updatePhone")
    public ResponseEntity<BaseResult> updatePhone(@RequestBody BankUserVo bankUser){
        if (StringValid.isBlank(bankUser.getUserPhone(),bankUser.getCode())){
            //验证手机号是否正确
            if (!NumberValid.verifyPhone(bankUser.getUserPhone())){
                return ResponseEntity.ok(new BaseResult(1, "该手机号不正确"));
            }

            String code = redisTemplate.opsForValue().get(bankUser.getUserPhone()+bankUser.getCode());
            if(StringUtils.isNotBlank(code)){
                //修改手机号之前先查询是否已经存在
                BankUser bankUserByUserPhone = userService.getBankUserByUserPhone(bankUser.getUserPhone());
                if (bankUserByUserPhone != null){
                    userService.updateBankUserPhoneToNull(bankUserByUserPhone);
                }
                //修改手机号
                BankUser user = userService.updateBankUserPhone(bankUser);
                BankUserVo bankUserVo = new BankUserVo();
                String phone = bankUser.getUserPhone();
                bankUserVo.setUserPhone(StarUtil.StringAddStar(phone,3,4));
                redisTemplate.delete(bankUser.getUserPhone()+bankUser.getCode());
                redisTemplate.delete(bankUser.getOldPhone());
                //将修改手机号之后的用户的信息保存到redis中，使用手机号作为key
                redisTemplate.opsForValue().set(user.getUserPhone(),JSONObject.toJSONString(user));
                //将修改手机号之后的用户的信息保存到redis中，使用用户id作为key
                redisTemplate.opsForValue().set("user-"+user.getUserId().toString()+"-userInfo", JSON.toJSONString(user));
                redisTemplate.opsForValue().set(user.getUserId().toString(),JSONObject.toJSONString(user));
                return ResponseEntity.ok(new BaseResult(0, "修改成功").append("user",bankUserVo));
            }
            return ResponseEntity.ok(new BaseResult(1, "验证码错误或验证码已经过时"));
        }
        return ResponseEntity.ok(new BaseResult(1, "验证码错误或验证码已经过时"));
    }

    /**
     * @author pds
     * @param file
     * @return org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description 实名认证
     * @date 2019/8/15 9:21
     */
    @PostMapping("/verifiedIdentity")
    public ResponseEntity<BaseResult> verifiedIdentity(@RequestParam(value = "file") List<MultipartFile> file,@RequestParam("userId") Integer userId) {
        if (file.size() == 2 && userId > 0){
            try {
                Integer identity = userService.verifiedIdentity(file, userId);
                if (identity == -2){
                    return ResponseEntity.ok(new BaseResult(2, "身份证已过期，认证失败"));
                }else if (identity == 0){
                    return ResponseEntity.ok(new BaseResult(1, "认证失败，请稍后重试"));
                }else if(identity == -3){
                    return ResponseEntity.ok(new BaseResult(3, "该身份证已被实名，请使用身份证登录旧账号，如不是您本人认证的，请到银行柜台进行解绑业务"));
                }
                BankUser bankUser = userService.getBankUserByUserId(userId);
                BankUserVo bankUserVo = new BankUserVo();
                String userName = bankUser.getUserName();
                if (StringUtils.isNotBlank(userName) && userName.length() > 1){
                    String first = userName.substring(0,1);
                    String end = userName.substring(userName.length()-1);
                    if (userName.length() == 2){
                        bankUserVo.setUserName(first+"*");
                    }else if(userName.length() >= 3){
                        StringBuffer stringBuffer = new StringBuffer();
                        for (int i = 0;i < userName.length()-2;i++){
                            stringBuffer.append("*");
                        }
                        bankUserVo.setUserName(first+stringBuffer.toString()+end);
                    }
                }
                if (StringUtils.isNotBlank(bankUser.getDefaultBankCard())){
                    bankUserVo.setDefaultBankCard(StarUtil.StringAddStar(bankUser.getDefaultBankCard(),6,4));
                } else {
                    bankUserVo.setDefaultBankCard("");
                }
                return ResponseEntity.ok(new BaseResult(0, "认证成功").append("user",bankUserVo));
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.ok(new BaseResult(1, "认证失败，请稍后重试"));
            } catch (ParseException e) {
                e.printStackTrace();
                return ResponseEntity.ok(new BaseResult(1, "认证失败，请稍后重试"));
            }
        }

        return ResponseEntity.ok(new BaseResult(1, "认证失败，请稍后重试"));
    }

    /**
     * @author pds
     * @param userId
     * @return com.zl.dc.vo.BaseResult
     * @description 退出登录
     * @date 2019/8/19 19:52
     */
    @GetMapping("/signOut")
    public BaseResult signOut(@RequestParam("userId") Integer userId){
        if (userId != null && userId > 0){
            BankUser user = userService.selectBankUserByUid(userId);
            Boolean phoneDelete = redisTemplate.delete(user.getUserPhone());
            Boolean userIdDelete = redisTemplate.delete(userId.toString());
            Boolean userIdUserInfo = redisTemplate.delete("user-" + user.getUserId().toString() + "-userInfo");
            if (userIdDelete && phoneDelete && userIdUserInfo){
                return new BaseResult(0, "退出登录成功");
            }
        }
        return new BaseResult(1, "退出登录失败");
    }

    /**
     * @author pds
     * @param bankUserVo
     * @return org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description 设置默认银行卡
     * @date 2019/8/19 19:53
     */
    @PostMapping("/setDefaultBankCard")
    public ResponseEntity<BaseResult> setDefaultBankCard(@RequestBody BankUserVo bankUserVo){
        if (StringUtils.isNotBlank(bankUserVo.getPassword())){
            Boolean set = userService.setDefaultBankCard(bankUserVo);
            if (set){
                BankUser user = userService.selectBankUserByUid(bankUserVo.getUserId());
                bankUserVo.setDefaultBankCard(StarUtil.StringAddStar(user.getDefaultBankCard(),6,4));
                bankUserVo.setBankCardId(null);
                bankUserVo.setPassword(null);
                redisTemplate.opsForValue().set("user-"+user.getUserId().toString()+"-userInfo", JSON.toJSONString(user));
                redisTemplate.opsForValue().set(user.getUserId().toString(), JSON.toJSONString(user));
                redisTemplate.opsForValue().set(user.getUserPhone(), JSON.toJSONString(user));
                return ResponseEntity.ok(new BaseResult(0, "设置成功").append("user",bankUserVo));
            }else {
                return ResponseEntity.ok(new BaseResult(1, "设置失败"));
            }
        }
        return ResponseEntity.ok(new BaseResult(1, "设置失败"));
    }
}
