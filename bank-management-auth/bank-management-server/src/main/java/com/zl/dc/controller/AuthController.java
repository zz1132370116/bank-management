package com.zl.dc.controller;




import com.zl.dc.pojo.BankUser;
import com.zl.dc.service.AuthService;
import com.zl.dc.vo.BaseResult;
import org.apache.commons.lang3.StringUtils;
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
     * 密码登录,生成token
     * @param user
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<BaseResult> login(@RequestBody BankUser user){
            if (user !=null) {
                //1登录操作--获得token
                String token = this.authService.login(user.getUserPhone(), user.getUserPassword(),user.getIdCard());
                //2 有token，返回
                if (StringUtils.isNotBlank(token)) {
                    BaseResult baseResult = new BaseResult(0, "登录成功").append("token", token);
                    return ResponseEntity.ok(baseResult);
                }
            }

        //3 没有token，失败
        return ResponseEntity.ok( new BaseResult(1 , "登录失败"));
    }
    /**
     * @author: zhanglei
     * @param: [user]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 功能描述
     * @data: 2019/8/5 19:14
     */
    @PostMapping("/loginBySendSms")
    public ResponseEntity<BaseResult> loginBySendSms(@RequestBody BankUser user){
        if (user !=null) {
            //1登录操作--获得token
            String token = this.authService.loginBySendSms(user.getUserPhone(), user.getUserPassword());
            //2 有token，返回
            if (StringUtils.isNotBlank(token)) {
                BaseResult baseResult = new BaseResult(0, "登录成功").append("token", token);
                return ResponseEntity.ok(baseResult);
            }
        }

        //3 没有token，失败
        return ResponseEntity.ok( new BaseResult(1 , "登录失败"));
    }
}
