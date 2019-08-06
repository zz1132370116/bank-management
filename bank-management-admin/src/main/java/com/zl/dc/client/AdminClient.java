package com.zl.dc.client;

import com.zl.dc.pojo.BankManager;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
  * @version: V1.0
  * @author: zhanglei
  * @className: AdminClient
  * @description: 远程调用
  * @data: 2019/8/6 9:11
  */
 @FeignClient("web-service")
public interface AdminClient {
    /**
     * @author: zhanglei
     * @param: [managerName, managerPassword]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.pojo.BankManager>
     * @description: 管理员登录
     * @data: 2019/8/6 11:22
     */
    @PostMapping("/getLogin")
    ResponseEntity<BankManager> getLogin(@RequestParam("managerName") String managerName, @RequestParam("managerPassword") String managerPassword);
}
