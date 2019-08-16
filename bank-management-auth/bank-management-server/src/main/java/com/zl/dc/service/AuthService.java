package com.zl.dc.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zl.dc.client.UserClient;
import com.zl.dc.config.JwtProperties;
import com.zl.dc.entity.UserInfo;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.util.JwtUtils;
import com.zl.dc.util.StarUtil;
import com.zl.dc.vo.BankUserVo;
import com.zl.dc.vo.BaseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @version V1.0
 * @author pds
 * @className AuthService
 * @description 普通用户注册登录功能
 * @date 2019/8/15 17:33
 */
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Resource
    private UserClient userClient;

    @Resource
    private JwtProperties jwtProperties;

    /**
     * @author pds
     * @param bankUserVo
     * @return com.zl.dc.vo.BaseResult
     * @description 注册----注册
     * @date 2019/8/14 10:31
     */
    public BaseResult registry(BankUserVo bankUserVo){
        try {
            BaseResult result = userClient.register(bankUserVo);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @author pds
     * @param bankUser
     * @return com.zl.dc.vo.BaseResult
     * @description 使用密码进行登录
     * @date 2019/8/14 9:17
     */
    public BaseResult login(BankUser bankUser) {
        try {
            BaseResult result = userClient.queryUser(bankUser);
            return loginMsg(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @author pds
     * @param bankUser
     * @return com.zl.dc.vo.BaseResult
     * @description 使用验证码进行登录
     * @date 2019/8/14 9:17
     */
    public BaseResult loginBySendSms(BankUserVo bankUser) {
        try {
            BaseResult result = userClient.loginBySendSms(bankUser);
            return loginMsg(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @author pds
     * @param baseResult
     * @return com.zl.dc.vo.BaseResult
     * @description 对登录返回的结果进行处理
     * @date 2019/8/14 9:18
     */
    public BaseResult loginMsg(BaseResult baseResult) throws Exception{
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

            String userName = bankUser.getUserName();
            if (StringUtils.isNotBlank(userName) && userName.length() > 1){
                String first = userName.substring(0,1);
                String end = userName.substring(userName.length()-1);
                if (userName.length() == 2){
                    bankUser.setUserName(first+"*");
                }else if(userName.length() >= 3){
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 0;i < userName.length()-2;i++){
                        stringBuffer.append("*");
                    }
                    bankUser.setUserName(first+stringBuffer.toString()+end);
                }
            }

            String phone = bankUser.getUserPhone();
            bankUser.setUserPassword(StarUtil.StringAddStar(phone,3,4));

            //将用户的一些信息置空
            bankUser.setUserPassword(null);
            bankUser.setIdCard(null);
            byte status = 0;
            bankUser.setUserStatus(status);
            bankUser.setDefaultBankCard("");
            baseResult.append("user",bankUser);
        }
        return baseResult;
    }

    public BaseResult logout(Integer userId) {
        try {
            BaseResult baseResult = userClient.signOut(userId);
            return baseResult;
        }catch (Exception e){
            e.printStackTrace();
        }
        return new BaseResult(1, "退出登录失败");
    }
}