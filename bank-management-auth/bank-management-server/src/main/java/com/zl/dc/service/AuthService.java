package com.zl.dc.service;


import com.zl.dc.client.UserClient;
import com.zl.dc.config.JwtProperties;
import com.zl.dc.entity.UserInfo;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.util.JwtUtils;
import com.zl.dc.vo.BankUserVo;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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
     * @return token值
     */
    public Map<String, Object> login(BankUser bankUser) {
        try {
            //1 登录--查询
            BankUser user = userClient.queryUser(bankUser);
            System.out.println(user);
            //2 如果不为空，生产token
            if (user != null) {
                return loginMsg(user);
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
    public Map<String, Object> loginBySendSms(BankUserVo bankUser) {
        try {
            //1 登录--查询
            BankUser user = userClient.loginBySendSms(bankUser);
            //2 如果不为空，生产token
            if (user != null) {
                return loginMsg(user);
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
     * @author pds
     * @param bankUser
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @description 将生成token的代码块提取出来
     * @date 2019/8/11 16:17
     */
    public Map<String,Object> loginMsg(BankUser bankUser) throws Exception{
        String token = JwtUtils.generateToken(new UserInfo(bankUser.getUserId(), bankUser.getUserName()), jwtProperties.getPrivateKey(), jwtProperties.getExpire());
        Map<String,Object> map = new HashMap<>(16);
        bankUser.setUserPassword(null);
        bankUser.setIdCard(null);
        byte status = 0;
        bankUser.setUserStatus(status);
        bankUser.setDefaultBankCardId(0);
        map.put("user",bankUser);
        map.put("token",token);
        return map;
    }
}