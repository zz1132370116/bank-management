package com.zl.dc.client;

import com.zl.dc.pojo.BankEnterprise;
import com.zl.dc.vo.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @version V1.0
 * @author pds
 * @className BankEnterpriseClient
 * @description 企业登录接口层
 * @date 2019/8/15 17:34
 */
@FeignClient("web-service")
public interface BankEnterpriseClient {
    /**
     * @author pds
     * @param bankEnterprise
     * @return com.zl.dc.vo.BaseResult
     * @description 企业登录接口
     * @date 2019/8/15 19:08
     */
    @PostMapping("/enterpiseLogin")
    BaseResult bankEnterpriseLogin(@RequestBody BankEnterprise bankEnterprise);

    /**
     * @author pds
     * @param enterpiseId
     * @return com.zl.dc.vo.BaseResult
     * @description 退出登录
     * @date 2019/8/17 11:32
     */
    @GetMapping("/enterpiseSignOut")
    BaseResult enterpiseSignOut(@RequestParam("enterpiseId") Integer enterpiseId);

}
