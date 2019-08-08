package com.zl.dc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.zl.dc.config.SmsChangePassword;
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
     * @description: 功能描述
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
        if (user.getUserPhone() != null && !"".equals(user.getUserPhone())){
            BankUser bankUser = userService.getBankUserByUserPhone(user.getUserPhone());
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
                smsResponse = SmsChangePassword.sendSms(user.getUserPhone(), code);
            }catch (ClientException e){
                e.printStackTrace();
                return ResponseEntity.ok(new BaseResult(1, "发送失败"));
            }
            String smsStatus = "OK";
            if (smsStatus.equalsIgnoreCase(smsResponse.getCode())) {
                return ResponseEntity.ok(new BaseResult(0, "发送成功"));
            } else {
                return ResponseEntity.ok(new BaseResult(0, smsResponse.getMessage()));
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
        boolean isPhone = user.getUserPhone() != null && !"".equals(user.getUserPhone());
        boolean isCode = user.getCode() != null && !"".equals(user.getCode());
        if (isPhone && isCode){
            //通过手机号获取验证码
            String code = redisTemplate.opsForValue().get(user.getUserPhone());
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
        System.out.println(user.getUserPhone()+"  "+user.getUserPassword());
        boolean isPhone = user.getUserPhone() != null && !"".equals(user.getUserPhone());
        boolean isPassword = user.getUserPassword() != null && !"".equals(user.getUserPassword());

        if (isPhone && isPassword){
            BankUser bankUser = userService.updateBankUserPassword(user);
            redisTemplate.opsForValue().set(bankUser.getUserPhone(),JSONObject.toJSONString(bankUser));
        }

        return ResponseEntity.ok(new BaseResult(0, "密码修改成功"));
    }
}
