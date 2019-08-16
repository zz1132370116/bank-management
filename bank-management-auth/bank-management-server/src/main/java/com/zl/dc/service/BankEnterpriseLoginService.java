package com.zl.dc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zl.dc.client.BankEnterpriseClient;
import com.zl.dc.config.JwtProperties;
import com.zl.dc.entity.UserInfo;
import com.zl.dc.pojo.BankEnterprise;
import com.zl.dc.util.JwtUtils;
import com.zl.dc.util.StarUtil;
import com.zl.dc.vo.BaseResult;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
/**
 * @version V1.0
 * @author pds
 * @className BankEnterpriseService
 * @description 企业登录业务层
 * @date 2019/8/15 19:16
 */
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class BankEnterpriseLoginService {
    @Resource
    private BankEnterpriseClient enterpriseClient;
    @Resource
    private JwtProperties jwtProperties;

    /**
     * @author pds
     * @param bankEnterprise
     * @return com.zl.dc.vo.BaseResult
     * @description 企业登录
     * @date 2019/8/16 10:19
     */
    public BaseResult bankEnterpriseLogin(BankEnterprise bankEnterprise){
        try {
            BaseResult baseResult = enterpriseClient.bankEnterpriseLogin(bankEnterprise);
            return enterpriseLoginMsg(baseResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @author pds
     * @param baseResult
     * @return com.zl.dc.vo.BaseResult
     * @description 处理登录后返回前端的信息
     * @date 2019/8/15 19:25
     */
    public BaseResult enterpriseLoginMsg(BaseResult baseResult) throws Exception{
        Integer errno = (Integer) baseResult.getData().get("errno");
        //如果为0，则是登录成功
        if (errno == 0){
            //将返回的用户信息提取出来
            Object enterpriseObj = baseResult.getData().get("enterprise");
            JSON json = (JSON) JSONObject.toJSON(enterpriseObj);
            BankEnterprise enterprise = JSONObject.toJavaObject(json,BankEnterprise.class);

            //生成token
            String enterpriseToken = JwtUtils.generateToken(new UserInfo(enterprise.getEnterpriseId(), enterprise.getEnterpriseName()), jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            baseResult.append("enterpriseToken",enterpriseToken);

            enterprise.setEnterpriseBankCard(StarUtil.StringAddStar(enterprise.getEnterpriseBankCard(),4,4));
            enterprise.setEnterpriseLoginPassword(null);
            enterprise.setEnterpriseRegno(null);
            Byte status = 0;
            enterprise.setEnterpriseStatus(status);
            enterprise.setLegalpersonIdCard(null);
            enterprise.setLegalpersonPhone(null);
            baseResult.append("enterprise",enterprise);
        }
        return baseResult;
    }
}
