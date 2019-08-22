package com.zl.dc.controller;

import com.zl.dc.pojo.BankEnterprise;
import com.zl.dc.service.BankEnterpriseLoginService;
import com.zl.dc.util.StringValid;
import com.zl.dc.vo.BaseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * @author pds
     * @param bankEnterprise
     * @return org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description 企业登录
     * @date 2019/8/16 10:19
     */
    @PostMapping("/bankEnterpriseLogin")
    public ResponseEntity<BaseResult> bankEnterpriseLogin(@RequestBody BankEnterprise bankEnterprise){
        if (StringValid.isBlank(bankEnterprise.getEnterpriseBankCard(),bankEnterprise.getEnterpriseLoginPassword())){
            BaseResult result = enterpriseService.bankEnterpriseLogin(bankEnterprise);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok(new BaseResult(1,"登录失败，账号或密码错误"));
    }

    /**
     * @author pds
     * @param enterpriseId
     * @return org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description 退出登录
     * @date 2019/8/16 10:24
     */
    @GetMapping("/enterpriseLogout/{enterpriseId}")
    public ResponseEntity<BaseResult> logout(@PathVariable("enterpriseId") Integer enterpriseId){
        if (enterpriseId != null && enterpriseId > 0){
            BaseResult logout = enterpriseService.enterpriseLogout(enterpriseId);
            return ResponseEntity.ok(logout);
        }
        return ResponseEntity.ok(new BaseResult(1, "退出登录失败"));
    }
}
