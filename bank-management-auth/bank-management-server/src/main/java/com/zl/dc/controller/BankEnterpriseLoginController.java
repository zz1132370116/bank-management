package com.zl.dc.controller;

import com.zl.dc.pojo.BankEnterprise;
import com.zl.dc.service.BankEnterpriseLoginService;
import com.zl.dc.vo.BaseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @version V1.0
 * @author pds
 * @className BankEnterpriseController
 * @description 企业登录控制层
 * @date 2019/8/15 17:36
 */
@RestController
public class BankEnterpriseLoginController {
    @Resource
    private BankEnterpriseLoginService enterpriseService;

    @PostMapping("/bankEnterpriseLogin")
    public ResponseEntity<BaseResult> bankEnterpriseLogin(@RequestBody BankEnterprise bankEnterprise){
        if (StringUtils.isNoneBlank(bankEnterprise.getEnterpriseBankCard(),bankEnterprise.getEnterpriseLoginPassword())){
            BaseResult result = enterpriseService.bankEnterpriseLogin(bankEnterprise);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok(new BaseResult(1,"登录失败，账号或密码错误"));
    }
}
