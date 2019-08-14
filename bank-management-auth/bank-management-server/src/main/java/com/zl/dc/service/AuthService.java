package com.zl.dc.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zl.dc.client.UserClient;
import com.zl.dc.config.JwtProperties;
import com.zl.dc.entity.UserInfo;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.util.JwtUtils;
import com.zl.dc.vo.BankUserVo;
import com.zl.dc.vo.BaseResult;
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
     * @return token值
     */
    public BaseResult login(BankUser bankUser) {
        try {
            //1 登录--查询
            BaseResult result = userClient.queryUser(bankUser);
            //对数据进行处理
            return loginMsg(result);
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
    public BaseResult loginBySendSms(BankUserVo bankUser) {
        try {
            //1 登录--查询
            BaseResult result = userClient.loginBySendSms(bankUser);
            //对数据进行处理
            return loginMsg(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //没有登录成功
        return null;
    }

    /**
     * @author pds
     * @param baseResult
     * @return com.zl.dc.vo.BaseResult
     * @description 对登录的返回结果进行处理
     * @date 2019/8/12 19:36
     */
    public BaseResult loginMsg(BaseResult baseResult) throws Exception{
        //获取返回的状态值
        Integer errno = (Integer) baseResult.getData().get("errno");
        //如果为0，则是登录成功
        if (errno == 0){
            //将返回的用户信息提取出来
            Object userObj = baseResult.getData().get("user");
            JSON json = (JSON) JSONObject.toJSON(userObj);
            BankUser bankUser = JSONObject.toJavaObject(json,BankUser.class);

            //生成token
            String token = JwtUtils.generateToken(new UserInfo(bankUser.getUserId(), bankUser.getUserName()), jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            baseResult.append("token",token);

            //将用户的一些信息置空
            bankUser.setUserPassword(null);
            bankUser.setIdCard(null);
            byte status = 0;
            bankUser.setUserStatus(status);
            bankUser.setDefaultBankCardId(0);
            baseResult.append("user",bankUser);
        }
        return baseResult;
    }
}