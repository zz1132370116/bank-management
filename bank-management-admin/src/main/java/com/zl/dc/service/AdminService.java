package com.zl.dc.service;

import com.zl.dc.client.AdminClient;
import com.zl.dc.pojo.BankManager;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.ManagerTranscation;
import com.zl.dc.pojo.TransferRecord;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

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
    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 查询会员数
     * @data: 2019/8/6 13:43
     */
    public List<BankUser> GetUserList() {
        List<BankUser> list = adminClient.GetUserList();
        return list;
    }
    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 查询记录数
     * @data: 2019/8/6 13:44
     */
    public List<TransferRecord> GetRecords() {
        List<TransferRecord> transferRecords = adminClient.GetRecords();
        return transferRecords;
    }
    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 查询异常数
     * @data: 2019/8/6 13:45
     */
    public List<ManagerTranscation> GetAbnormals() {
        List<ManagerTranscation> managerTranscations = adminClient.GetAbnormals();
        return managerTranscations;
    }
}
