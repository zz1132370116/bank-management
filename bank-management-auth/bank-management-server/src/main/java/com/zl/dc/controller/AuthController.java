package com.zl.dc.controller;


import com.zl.dc.pojo.BankUser;
import com.zl.dc.service.AuthService;
import com.zl.dc.vo.BankUserVo;
import com.zl.dc.vo.BaseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by 舍頭襘游泳 on 2018/12/13.
 */
@RestController
@RequestMapping
public class AuthController {
    @Resource
    private AuthService authService;

    /**
     * @author: zhanglei
     * @param: [bankUser]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 根据密码登录
     * @data: 2019/8/5 20:12
     */
    @PostMapping("/login")
    public ResponseEntity<BaseResult> login(@RequestBody BankUser bankUser) {
        if (bankUser != null) {
            //如果是使用手机加密码登录
            if (bankUser.getUserPassword() != null && !"".equals(bankUser.getUserPhone())){
                //验证手机号是否正确
                String regex = "^[1][3,4,5,7,8][0-9]{9}$";
                if (!bankUser.getUserPhone().matches(regex)){
                    return ResponseEntity.ok(new BaseResult(1, "该手机号不正确"));
                }
            }else if(bankUser.getIdCard() != null && !"".equals(bankUser.getIdCard())){
                //验证身份证是否正确
                String regex = "(^\\d{18}$)|(^\\d{15}$)";
                if (!bankUser.getIdCard().matches(regex)){
                    return ResponseEntity.ok(new BaseResult(1, "该身份证号不正确"));
                }
            }
            /*//1登录操作--获得token和用户信息
            Map<String, Object> map = this.authService.login(bankUser);
            String token = (String) map.get("token");
            BankUser user = (BankUser) map.get("user");
            //2 有token，返回
            if (StringUtils.isNotBlank(token)) {
                BaseResult baseResult = new BaseResult(0, "登录成功").append("token", token).append("user",user);
                return ResponseEntity.ok(baseResult);
            }*/
            if (bankUser.getUserPassword() == null || "".equals(bankUser.getUserPassword())){
                return ResponseEntity.ok(new BaseResult(1, "密码不能为空"));
            }
            //登录
            BaseResult baseResult = this.authService.login(bankUser);
            return ResponseEntity.ok(baseResult);
        }
        //3 没有token，失败
        return ResponseEntity.ok(new BaseResult(1, "登录失败"));
    }

    /**
     * @author: zhanglei
     * @param: [bankUser]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 根据验证码登录
     * @data: 2019/8/5 19:14
     */
    @PostMapping("/loginBySendSms")
    public ResponseEntity<BaseResult> loginBySendSms(@RequestBody BankUserVo bankUser) {
        if (bankUser != null) {
            //判断验证码是否为空
            if (bankUser.getCode() == null || "".equals(bankUser.getCode())) {
                return ResponseEntity.ok(new BaseResult(1, "验证码不能为空"));
            }
            /*//1登录操作--获得token和用户信息
            Map<String, Object> map = this.authService.loginBySendSms(bankUser);
            BankUser user = (BankUser) map.get("user");
            String token = (String) map.get("token");
            //2 有token，返回
            if (StringUtils.isNotBlank(token)) {
                BaseResult baseResult = new BaseResult(0, "登录成功")
                        .append("token", token).append("user",user);
                return ResponseEntity.ok(baseResult);
            }*/

            //登录
            BaseResult baseResult = this.authService.loginBySendSms(bankUser);
            return ResponseEntity.ok(baseResult);
        }
        //3 没有token，失败
        return ResponseEntity.ok(new BaseResult(1, "登录失败"));
    }
}
