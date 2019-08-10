package com.zl.dc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.zl.dc.config.SmsChangePassword;
import com.zl.dc.config.SmsChangePhone;
import com.zl.dc.config.SmsLogin;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.service.UserService;
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
    public ResponseEntity<BaseResult> queryUser(@RequestBody BankUser bankUser) {
        BankUser user = null;
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
            if (user.getUserPassword().equals(bankUser.getUserPassword())) {
                //将用户信息存redis,user对象转为字符串
                redisTemplate.opsForValue().set(user.getUserPhone(), JSON.toJSONString(user));
                return ResponseEntity.ok(new BaseResult(0, "登录成功").append("data", user));
            }
        }
        //3 正确
        return ResponseEntity.ok(new BaseResult(1, "登录失败"));
    }

    /**
     * @author: zhanglei
     * @param: [bankUser]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 根据验证码登录
     * @data: 2019/8/5 19:20
     */
    @PostMapping("/loginBySendSms")
    public ResponseEntity<BaseResult> loginBySendSms(@RequestBody BankUser bankUser) {
        //通过手机号获取验证码
        String s = redisTemplate.opsForValue().get(bankUser.getUserPhone());
        if (!s.equals("")) {
            //通过手机号查询用户信息
            BankUser bankUserByUserPhone = userService.getBankUserByUserPhone(bankUser.getUserPhone());
            //将用户信息存到redis
            redisTemplate.opsForValue().set(bankUser.getUserPhone(), JSON.toJSONString(bankUserByUserPhone));
            return ResponseEntity.ok(new BaseResult(0, "登录成功"));
        }
        //3 失败
        return ResponseEntity.ok(new BaseResult(1, "验证码错误"));
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
                //存redis5分钟
                redisTemplate.opsForValue().set(bankUser.getUserPhone(), code, 5, TimeUnit.MINUTES);
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
    public ResponseEntity<BaseResult> updatePasswordSms(@RequestBody BankUser user){
        System.out.println(user.getUserPhone());
        //判断手机号是否为空
        if (user.getUserPhone() != null && !"".equals(user.getUserPhone())){
            //从数据库中根据手机号获取对应的用户信息，或许可以从redis中获取
            BankUser bankUser = userService.getBankUserByUserPhone(user.getUserPhone());
            //如果不存在此用户
            if (bankUser == null){
                return ResponseEntity.ok(new BaseResult(1, "该手机未注册账号，请重新输入"));
            }
            //发送短信
            //1、生成验证码
            String code = RandomStringUtils.randomNumeric(4);
            //2、存redis中5分钟
            redisTemplate.opsForValue().set(user.getUserPhone(),code,5,TimeUnit.MINUTES);
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
     * @author: pds
     * @params:  [user]
     * @return: org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 修改密码----验证验证码是否正确
     * @data: 2019/8/7 16:28
     */
    @PostMapping("/updatePasswordVerify")
    public ResponseEntity<BaseResult> updatePasswordVerify(@RequestBody BankUser user){
        System.out.println(user.getUserPhone()+"   "+user.getCode());
        //判断手机号是否为空
        boolean isPhone = user.getUserPhone() != null && !"".equals(user.getUserPhone());
        //判断验证码是否为空
        boolean isCode = user.getCode() != null && !"".equals(user.getCode());
        if (isPhone && isCode){
            //通过手机号从redis中获取验证码
            String code = redisTemplate.opsForValue().get(user.getUserPhone());
            //判断传过来的验证码和从redis中获取的验证码是否一致
            if(user.getCode().equals(code)){
                return ResponseEntity.ok(new BaseResult(0, "验证码正确"));
            }
            return ResponseEntity.ok(new BaseResult(1, "验证码错误或验证码已经过时"));
        }
        return ResponseEntity.ok(new BaseResult(1, "验证码错误或验证码已经过时"));
    }

    /**
     * @author: pds
     * @params:  [user]
     * @return: org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 修改密码----修改密码
     * @data: 2019/8/7 16:32
     */
    @PostMapping("/updateBankUserPassword")
    public ResponseEntity<BaseResult> updateBankUserPassword(@RequestBody BankUser user){
        System.out.println(user.getUserPhone()+"  "+user.getUserPassword()+"  "+user.getPasswordConfig());
        //判断手机号是否为空
        boolean isPhone = user.getUserPhone() != null && !"".equals(user.getUserPhone());
        //判断新密码是否为空
        boolean isPassword = user.getUserPassword() != null && !"".equals(user.getUserPassword());
        //判断确认新密码是否为空
        boolean isPasswordConfig = user.getPasswordConfig() != null && !"".equals(user.getPasswordConfig());
        //判断三个参数是否同时存在
        if (isPhone && isPassword && isPasswordConfig){
            //判断新密码和确认新密码是否相等
            boolean equal = user.getUserPassword().equals(user.getPasswordConfig());
            //如果相等则执行下面的操作
            if (equal){
                //执行修改密码操作
                BankUser bankUser = userService.updateBankUserPassword(user);
                //将修改密码后的用户信息保存到redis中
                redisTemplate.opsForValue().set(bankUser.getUserPhone(),JSONObject.toJSONString(bankUser));
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
     * @author: pds
     * @params:  [user]
     * @return: org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 修改手机----获取验证码
     * @data: 2019/8/8 16:04
     */
    @PostMapping("/updatePhoneSms")
    public ResponseEntity<BaseResult> updatePhoneSms(@RequestBody BankUser user){
        System.out.println(user.getUserPhone());
        boolean isOldPhone = user.getOldPhone() != null && !"".equals(user.getOldPhone());
        boolean isUserPhone = user.getUserPhone() != null && !"".equals(user.getUserPhone());

        //判断手机号是否为空
        if (isOldPhone || isUserPhone){
            //如果oldPhone不为空，而userPhone为空，那么就是第一次获取验证码，此时的oldPhone是数据库里存在的，
            // 是这个账号当前绑定的，因此需要判断此时输入的手机号是否是当前账号绑定的。
            if(isOldPhone && !isUserPhone){
                //从数据库中根据手机号获取对应的用户信息，或许可以从redis中获取
                BankUser bankUser = userService.getBankUserByUserPhone(user.getOldPhone());
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
                this.updatePhoneSms(user.getUserPhone());
            }
        }
        return ResponseEntity.ok(new BaseResult(0, "请稍后再点"));
    }

    /**
     * @author: pds
     * @params:  [phone]
     * @return: org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 由于修改手机号要获取两次验证码，因此将获取验证码这部分代码提取出来
     * @data: 2019/8/10 13:53
     */
    public ResponseEntity<BaseResult> updatePhoneSms(String phone){
        //发送短信
        //1、生成验证码
        String code = RandomStringUtils.randomNumeric(4);
        //2、存redis中5分钟
        redisTemplate.opsForValue().set(phone,code,5,TimeUnit.MINUTES);
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
     * @author: pds
     * @params:  [user]
     * @return: org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 修改手机----验证验证码
     * @data: 2019/8/8 16:08
     */
    @PostMapping("/updatePhoneVerify")
    public ResponseEntity<BaseResult> updatePhoneVerify(@RequestBody BankUser user){
        System.out.println(user.getOldPhone()+"   "+user.getCode());
        //判断手机号是否为空
        boolean isPhone = user.getOldPhone() != null && !"".equals(user.getOldPhone());
        //判断验证码是否为空
        boolean isCode = user.getCode() != null && !"".equals(user.getCode());
        //判断两者是否为空
        if (isPhone && isCode){
            //通过手机号从redis中获取验证码
            String code = redisTemplate.opsForValue().get(user.getOldPhone());
            //判断传过来的验证码和从redis中获取的验证码是否一致
            if(user.getCode().equals(code)){
                return ResponseEntity.ok(new BaseResult(0, "验证码正确"));
            }
            return ResponseEntity.ok(new BaseResult(1, "验证码错误或验证码已经过时"));
        }
        return ResponseEntity.ok(new BaseResult(0, "验证码错误或验证码已经过时"));
    }

    /**
     * @author: pds
     * @params:  [bankUser]
     * @return: org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 修改手机----修改手机
     * @data: 2019/8/10 9:12
     */
    @PostMapping("/updatePhone")
    public ResponseEntity<BaseResult> updatePhone(BankUser bankUser){
        boolean isPhone = bankUser.getUserPhone() != null && !"".equals(bankUser.getUserPhone());
        boolean isCode = bankUser.getCode() != null && !"".equals(bankUser.getCode());
        if (isPhone && isCode){
            //通过手机号从redis中获取验证码
            String code = redisTemplate.opsForValue().get(bankUser.getUserPhone());
            //判断传过来的验证码和从redis中获取的验证码是否一致
            if(bankUser.getCode().equals(code)){
                //修改手机号
                BankUser user = userService.updateBankUserPhone(bankUser);
                //将修改手机号之后的用户的信息保存到redis中
                redisTemplate.opsForValue().set(user.getUserPhone(),JSONObject.toJSONString(user));
                return ResponseEntity.ok(new BaseResult(0, "修改成功"));
            }
            return ResponseEntity.ok(new BaseResult(1, "验证码错误或验证码已经过时"));
        }
        return ResponseEntity.ok(new BaseResult(1, "验证码错误或验证码已经过时"));
    }
}
