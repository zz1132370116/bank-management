package com.zl.dc.controller;

import com.alibaba.fastjson.JSON;
import com.zl.dc.pojo.BankEnterprise;
import com.zl.dc.service.BankEnterpriseService;
import com.zl.dc.util.MD5;
import com.zl.dc.vo.BaseResult;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @version V1.0
 * @author pds
 * @className BankEnterpriseController
 * @description 企业控制层
 * @date 2019/8/15 19:58
 */
@RestController
public class BankEnterpriseController {
    @Resource
    private BankEnterpriseService bankEnterpriseService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * @author pds
     * @param bankEnterprise
     * @return com.zl.dc.vo.BaseResult
     * @description 企业登录
     * @date 2019/8/16 10:18
     */
    @PostMapping("/enterpiseLogin")
    public BaseResult enterpiseLogin(@RequestBody BankEnterprise bankEnterprise){
        BankEnterprise enterpriseBankCard = bankEnterpriseService.getBankEnterpriseByEnterpriseBankCard(bankEnterprise.getEnterpriseBankCard());
        if (enterpriseBankCard != null){
            String password = MD5.GetMD5Code(bankEnterprise.getEnterpriseLoginPassword());
            if (password.equals(enterpriseBankCard.getEnterpriseLoginPassword())){
                stringRedisTemplate.opsForValue().set(enterpriseBankCard.getEnterpriseName(), JSON.toJSONString(bankEnterprise));
                enterpriseBankCard.setGmtCreate(null);
                enterpriseBankCard.setGmtModified(null);
                return new BaseResult(0,"登录成功").append("enterprise",enterpriseBankCard);
            }
        }
        return new BaseResult(1,"登录失败，账号或密码错误");
    }
}
