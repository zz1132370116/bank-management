package com.zl.dc.service;

import com.zl.dc.client.AdminClient;
import com.zl.dc.pojo.BankManager;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
  * @version: V1.0
  * @author: zhanglei
  * @className: AdminService
  * @description: 管理员操作层
  * @data: 2019/8/6 9:09
  */
 @Service
public class AdminService {
     @Resource
    private AdminClient adminClient;
    /**
     * @author: zhanglei
     * @param: [bankManager]
     * @return:com.zl.dc.pojo.BankManager
     * @description: 管理员登录
     * @data: 2019/8/6 11:25
     */
    public BankManager getLogin(BankManager bankManager) {
       BankManager bankManager1 = adminClient.getLogin(bankManager.getManagerName(),bankManager.getManagerPassword()).getBody();
       return bankManager1;
    }
}
