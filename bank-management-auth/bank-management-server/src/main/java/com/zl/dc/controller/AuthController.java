package com.zl.dc.controller;


import com.zl.dc.pojo.BankUser;
import com.zl.dc.service.AuthService;
import com.zl.dc.vo.BankUserVo;
import com.zl.dc.vo.BaseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @version V1.0
 * @author pds
 * @className AuthController
 * @description 普通用户登录注册功能
 * @date 2019/8/15 17:29
 */
@RestController
@RequestMapping
public class AuthController {
    @Resource
    private AuthService authService;

    /**
     * @author pds
     * @param bankUserVo
     * @return org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description 注册----注册
     * @date 2019/8/14 9:29
     */
    @PostMapping("/registry")
    public ResponseEntity<BaseResult> registry(@RequestBody BankUserVo bankUserVo){

        if (StringUtils.isNoneBlank(bankUserVo.getUserPhone(),bankUserVo.getUserPassword(),bankUserVo.getPasswordConfig(),bankUserVo.getCode())){
            if (!bankUserVo.getUserPassword().equals(bankUserVo.getPasswordConfig())){
                return ResponseEntity.ok(new BaseResult(2,"确认密码与密码不相等"));
            }
            String regex = "^[1][3,4,5,7,8][0-9]{9}$";
            if (!bankUserVo.getUserPhone().matches(regex)){
                return ResponseEntity.ok(new BaseResult(3,"手机格式不正确"));
            }
            BaseResult baseResult = authService.registry(bankUserVo);
            return ResponseEntity.ok(baseResult);
        }
        return ResponseEntity.ok(new BaseResult(1,"注册失败"));
    }


    /**
     * @author pds
     * @param bankUser
     * @return org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description 登录----密码登录
     * @date 2019/8/20 11:09
     */
    @PostMapping("/login")
    public ResponseEntity<BaseResult> login(@RequestBody BankUser bankUser) {
        if (bankUser != null) {
            //如果是使用手机加密码登录
            if (StringUtils.isNotBlank(bankUser.getUserPhone())){
                //验证手机号是否正确
                String regex = "^[1][3,4,5,7,8][0-9]{9}$";
                if (!bankUser.getUserPhone().matches(regex)){
                    return ResponseEntity.ok(new BaseResult(4, "该手机号不正确"));
                }
            }else if(StringUtils.isNotBlank(bankUser.getIdCard())){
                //验证身份证是否正确
                String regex = "(^\\d{18}$)|(^\\d{15}$)";
                if (!bankUser.getIdCard().matches(regex)){
                    return ResponseEntity.ok(new BaseResult(4, "该身份证号不正确"));
                }
            }

            if (bankUser.getUserPassword() == null || "".equals(bankUser.getUserPassword())){
                return ResponseEntity.ok(new BaseResult(5, "密码不能为空"));
            }
            //登录
            BaseResult baseResult = this.authService.login(bankUser);
            return ResponseEntity.ok(baseResult);
        }
        //3 没有token，失败
        return ResponseEntity.ok(new BaseResult(2, "登录失败，请重试"));
    }

    /**
     * @author pds
     * @param bankUser
     * @return org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description 登录----验证码登录
     * @date 2019/8/20 11:08
     */
    @PostMapping("/loginBySendSms")
    public ResponseEntity<BaseResult> loginBySendSms(@RequestBody BankUserVo bankUser) {
        if (bankUser != null) {
            if (StringUtils.isNotBlank(bankUser.getUserPhone())){
                //验证手机号是否正确
                String regex = "^[1][3,4,5,7,8][0-9]{9}$";
                if (!bankUser.getUserPhone().matches(regex)){
                    return ResponseEntity.ok(new BaseResult(4, "该手机号不正确"));
                }
            }
            //判断验证码是否为空
            if (!StringUtils.isNotBlank(bankUser.getCode())) {
                return ResponseEntity.ok(new BaseResult(3, "验证码不能为空"));
            }

            //登录
            BaseResult baseResult = this.authService.loginBySendSms(bankUser);
            return ResponseEntity.ok(baseResult);
        }
        //3 没有token，失败
        return ResponseEntity.ok(new BaseResult(2, "登录失败"));
    }

    /**
     * @author pds
     * @param userId
     * @return org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description 用户退出登录
     * @date 2019/8/16 10:24
     */
    @GetMapping("/logout/{userId}")
    public ResponseEntity<BaseResult> logout(@PathVariable("userId") Integer userId){
        if (userId != null && userId > 0){
            BaseResult logout = authService.logout(userId);
            return ResponseEntity.ok(logout);
        }
        return ResponseEntity.ok(new BaseResult(1, "退出登录失败"));
    }
}
