package com.zl.dc.service;


import com.zl.dc.client.UserClient;
import com.zl.dc.config.JwtProperties;
import com.zl.dc.entity.UserInfo;
import com.zl.dc.util.JwtUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by 舍頭襘游泳 on 2018/12/13.
 */
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Resource
    private UserClient userClient;

    @Resource
    private JwtProperties jwtProperties;

    /**
     * 登录操作
     *
     * @param phone    手机号
     * @param password 密码
     * @return token值
     */
    public String login(String phone, String password, String idCard) {
        try {
            //1 登录--查询
            BankUser user = userClient.queryUser(phone, password, idCard).getBody();
            //2 如果不为空，生产token
            if (user != null) {
                return JwtUtils.generateToken(new UserInfo(user.getUserId(), user.getUserName()), jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            }
            //3 如果为空
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //没有登录成功
        return null;
    }

    /**
     * @author: zhanglei
     * @param: [userPhone, userPassword]
     * @return:java.lang.String
     * @description: 通过验证码登录
     * @data: 2019/8/5 19:15
     */
    public String loginBySendSms(String userPhone, String userPassword) {
        try {
            //1 登录--查询
            BankUser user = userClient.loginBySendSms(userPhone, userPassword).getBody();
            //2 如果不为空，生产token
            if (user != null) {
                return JwtUtils.generateToken(new UserInfo(user.getUserId(), user.getUserName()), jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            }
            //3 如果为空
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //没有登录成功
        return null;
    }
}
