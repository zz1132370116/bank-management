package com.zl.dc.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.zl.dc.config.SmsChangePassword;
import com.zl.dc.config.SmsChangePhone;
import com.zl.dc.config.SmsLogin;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.service.UserService;
import com.zl.dc.util.BankUserPasswordUtil;
import com.zl.dc.util.NumberValid;
import com.zl.dc.vo.BankUserVo;
import com.zl.dc.vo.BaseResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @version: V1.0
 * @author: zhanglei
 * @className: 用户Controller
 * @description: TODO
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
     * @author: zhanglei
     * @param: [bankUser]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 通过手机号或身份证号登录
     * @data: 2019/8/5 17:05
     */
    @PostMapping("/query")
    public BankUser queryUser(@RequestBody BankUserVo bankUser) {
        System.out.println(bankUser);
        BankUser user;
        //1 通过手机号查询用户
        if (bankUser.getUserPhone().equals("")) {
            //通过身份证号查询用户信息
            user = this.userService.getBankUserByIdCard(bankUser.getIdCard());
        } else {
            //通过手机号查询用户信息
            user = this.userService.getBankUserByUserPhone(bankUser.getUserPhone());
        }
        //非空判断
        if (user != null) {
            //校验密码
            boolean verifyPassword = BankUserPasswordUtil.verify(bankUser.getUserPassword(), user.getUserPassword());
            //密码正确
            if (verifyPassword) {
                //将修改手机号之后的用户的信息保存到redis中，使用手机号作为key
                redisTemplate.opsForValue().set(user.getUserPhone(),JSONObject.toJSONString(user));
                //将修改手机号之后的用户的信息保存到redis中，使用用户id作为key
                redisTemplate.opsForValue().set(user.getUserId().toString(),JSONObject.toJSONString(user));
                return user;
            }else{
                //密码错误，密码错误次数达到3次之后会暂时将账号冻结，冻结24个小时，在冻结时间之内不允许该用户登录

            }
        }
        //3 失败
        return null;
    }

    /**
     * @author: zhanglei
     * @param: [bankUser]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 根据验证码登录
     * @data: 2019/8/5 19:20
     */
    @PostMapping("/loginBySendSms")
    public BankUser loginBySendSms(@RequestBody BankUserVo bankUser) {
        //通过手机号+验证码从redis中获取验证码
        String code = redisTemplate.opsForValue().get(bankUser.getUserPhone()+bankUser.getCode());
        //判断传过来的验证码是否正确
        if (code != null){
            //通过手机号查询用户信息
            BankUser bankUserByUserPhone = userService.getBankUserByUserPhone(bankUser.getUserPhone());
            //将修改手机号之后的用户的信息保存到redis中，使用手机号作为key
            redisTemplate.opsForValue().set(bankUserByUserPhone.getUserPhone(),JSONObject.toJSONString(bankUserByUserPhone));
            //将修改手机号之后的用户的信息保存到redis中，使用用户id作为key
            redisTemplate.opsForValue().set(bankUserByUserPhone.getUserId().toString(),JSONObject.toJSONString(bankUserByUserPhone));
            return bankUserByUserPhone;
        }
        //3 失败
        return null;
    }

    /**
     * @author: zhanglei
     * @param: []
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 发短信
     * @data: 2019/8/5 19:21
     */
    @PostMapping("/sendSms")
    public ResponseEntity<BaseResult> sendSms(@RequestBody BankUser bankUser) {
        try {
            if (!bankUser.getUserPhone().equals("")) {
                //发送短信
                //1 生产验证码
                String code = RandomStringUtils.randomNumeric(4);
                //存redis5分钟，因为用户的信息是使用手机号为key存放到redis中的，为了防止将用户信息覆盖，
                // 因此将这里的key设置为手机号+验证码
                redisTemplate.opsForValue().set(bankUser.getUserPhone()+code, code, 5, TimeUnit.MINUTES);
                System.out.println("手机验证码为:" + code);
                //3 发送短信
                SendSmsResponse smsResponse = SmsLogin.sendSms(bankUser.getUserPhone(), code);
                if ("OK".equalsIgnoreCase(smsResponse.getCode())) {
                    return ResponseEntity.ok(new BaseResult(0, "发送成功"));
                } else {
                    return ResponseEntity.ok(new BaseResult(0, smsResponse.getMessage()));
                }
            } else {
                return ResponseEntity.ok(new BaseResult(0, "请稍后再点"));
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
        System.out.println(user.getUserPhone());
        //判断手机号是否为空
        if (user.getUserPhone() != null && !"".equals(user.getUserPhone())){
            //验证手机号是否正确
            if (!NumberValid.verifyPhone(user.getUserPhone())){
                return ResponseEntity.ok(new BaseResult(1, "该手机号不正确"));
            }

            //从数据库中根据手机号获取对应的用户信息，或许可以从redis中获取
            BankUser bankUser = userService.getBankUserByUserPhone(user.getUserPhone());
            /*String bankUserStr = redisTemplate.opsForValue().get(user.getUserPhone());
            JSONObject bankUserJson = JSONObject.parseObject(bankUserStr);
            BankUser bankUser1 = bankUserJson.toJavaObject(BankUser.class);*/

            //如果不存在此用户
            if (bankUser == null){
                return ResponseEntity.ok(new BaseResult(1, "该手机未注册账号，请重新输入"));
            }
            //发送短信
            //1、生成验证码
            String code = RandomStringUtils.randomNumeric(4);
            //存redis5分钟，因为用户的信息是使用手机号为key存放到redis中的，为了防止将用户信息覆盖，
            // 因此将这里的key设置为手机号+验证码
            redisTemplate.opsForValue().set(user.getUserPhone()+code,code,5,TimeUnit.MINUTES);
            //3、发送短信
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
        System.out.println(user.getUserPhone()+"   "+user.getCode());
        //判断手机号是否为空
        boolean isPhone = user.getUserPhone() != null && !"".equals(user.getUserPhone());
        //判断验证码是否为空
        boolean isCode = user.getCode() != null && !"".equals(user.getCode());
        if (isPhone && isCode){
            //验证手机号是否正确
            if (!NumberValid.verifyPhone(user.getUserPhone())){
                return ResponseEntity.ok(new BaseResult(1, "该手机号不正确"));
            }

            //通过手机号+验证码从redis中获取验证码
            String code = redisTemplate.opsForValue().get(user.getUserPhone()+user.getCode());
            //判断传过来的验证码和从redis中获取的验证码是否一致
            if(code != null && !"".equals(code)){
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
        System.out.println(user.getUserPhone()+"  "+user.getUserPassword()+"  "+user.getPasswordConfig());
        //判断手机号是否为空
        boolean isPhone = user.getUserPhone() != null && !"".equals(user.getUserPhone());
        //判断新密码是否为空
        boolean isPassword = user.getUserPassword() != null && !"".equals(user.getUserPassword());
        //判断确认新密码是否为空
        boolean isPasswordConfig = user.getPasswordConfig() != null && !"".equals(user.getPasswordConfig());
        //判断三个参数是否同时存在
        if (isPhone && isPassword && isPasswordConfig){
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
                //将修改手机号之后的用户的信息保存到redis中，使用手机号作为key
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
        System.out.println(user.getUserPhone());
        boolean isOldPhone = user.getOldPhone() != null && !"".equals(user.getOldPhone());
        if (isOldPhone){
            //验证旧手机号是否正确
            if (!NumberValid.verifyPhone(user.getOldPhone())){
                return ResponseEntity.ok(new BaseResult(1, "该手机号不正确"));
            }
        }
        boolean isUserPhone = user.getUserPhone() != null && !"".equals(user.getUserPhone());
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
                //从数据库中根据手机号获取对应的用户信息，或许可以从redis中获取
                BankUser bankUser = userService.getBankUserByUserPhone(user.getOldPhone());
                /*String bankUserStr = redisTemplate.opsForValue().get(user.getOldPhone());
                JSONObject bankUserJson = JSONObject.parseObject(bankUserStr);
                BankUser bankUser1 = bankUserJson.toJavaObject(BankUser.class);*/

                System.out.println(bankUser);
                //如果不存在此用户
                if (bankUser == null){
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
        String code = RandomStringUtils.randomNumeric(4);
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
        System.out.println(user.getOldPhone()+"   "+user.getCode());
        //判断原手机号是否为空
        boolean isPhone = user.getOldPhone() != null && !"".equals(user.getOldPhone());
        //判断验证码是否为空
        boolean isCode = user.getCode() != null && !"".equals(user.getCode());
        //判断两者是否为空
        if (isPhone && isCode){
            //验证手机号是否正确
            if (!NumberValid.verifyPhone(user.getOldPhone())){
                return ResponseEntity.ok(new BaseResult(1, "该手机号不正确"));
            }

            //通过手机号+验证码从redis中获取验证码
            String code = redisTemplate.opsForValue().get(user.getOldPhone()+user.getCode());
            //判断传过来的验证码和从redis中获取的验证码是否一致
            if(code != null && !"".equals(code)){
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
    public ResponseEntity<BaseResult> updatePhone(BankUserVo bankUser){
        //手机号是否为空
        boolean isPhone = bankUser.getUserPhone() != null && !"".equals(bankUser.getUserPhone());
        //验证码是否为空
        boolean isCode = bankUser.getCode() != null && !"".equals(bankUser.getCode());
        if (isPhone && isCode){
            //验证手机号是否正确
            if (!NumberValid.verifyPhone(bankUser.getUserPhone())){
                return ResponseEntity.ok(new BaseResult(1, "该手机号不正确"));
            }

            //通过手机号+验证码从redis中获取验证码
            String code = redisTemplate.opsForValue().get(bankUser.getUserPhone()+bankUser.getCode());
            //判断传过来的验证码和从redis中获取的验证码是否一致
            if(code != null && !"".equals(code)){
                //修改手机号之前先查询是否已经存在
                BankUser bankUserByUserPhone = userService.getBankUserByUserPhone(bankUser.getUserPhone());
                if (bankUserByUserPhone != null){
                    userService.updateBankUserPhoneToEmpty(bankUserByUserPhone);
                }
                //修改手机号
                BankUser user = userService.updateBankUserPhone(bankUser);
                //将修改手机号之后的用户的信息保存到redis中，使用手机号作为key
                redisTemplate.opsForValue().set(user.getUserPhone(),JSONObject.toJSONString(user));
                //将修改手机号之后的用户的信息保存到redis中，使用用户id作为key
                redisTemplate.opsForValue().set(user.getUserId().toString(),JSONObject.toJSONString(user));
                return ResponseEntity.ok(new BaseResult(0, "修改成功"));
            }
            return ResponseEntity.ok(new BaseResult(1, "验证码错误或验证码已经过时"));
        }
        return ResponseEntity.ok(new BaseResult(1, "验证码错误或验证码已经过时"));
    }


}
