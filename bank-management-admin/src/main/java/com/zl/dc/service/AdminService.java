package com.zl.dc.service;

import com.zl.dc.client.AdminClient;
import com.zl.dc.pojo.BankManager;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.ManagerTranscation;
import com.zl.dc.pojo.TransferRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.Date;
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
        if (!bankManager.getManagerPassword().equals("") && !bankManager.getManagerName().equals("")){
            BankManager bankManager1 = adminClient.getLogin(bankManager.getManagerName(),bankManager.getManagerPassword()).getBody();
            return bankManager1;
        }

       return null;
    }
    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 查询会员数
     * @data: 2019/8/6 13:43
     */
    public List<BankUser> GetUserList(Integer pageNum) {
        List<BankUser> list = adminClient.GetUserList(pageNum);
        return list;
    }
    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 查询转账记录
     * @data: 2019/8/6 13:43
     */
    public List<TransferRecord> GetRecords(Integer pageNum) {
        List<TransferRecord> list = adminClient.GetRecords(pageNum);
        return list;
    }



    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 通过redis获取用户登录信息
     * @data: 2019/8/6 15:21
     */
    public BankManager GetUserByRedis() {
        BankManager bankManagers = adminClient.GetUserByRedis().getBody();
        return bankManagers;
    }
    /**
     * @author: zhanglei
     * @param: [idCard, startDate, endDate]
     * @return:java.util.List<com.zl.dc.pojo.TransferRecord>
     * @description: 条件查询记录
     * @data: 2019/8/6 19:11
     */
    public List<TransferRecord> getRecordsByParams(@RequestBody TransferRecord transferRecord) {
        List<TransferRecord> transferRecords =adminClient.getRecordsByParams(transferRecord);
        if (transferRecords !=null){
            return transferRecords;
        }
        return null;
    }
    /**
     * @author: zhanglei
     * @param: [userName, idCard]
     * @return:java.util.List<com.zl.dc.pojo.BankUser>
     * @description: 通过条件查询用户
     * @data: 2019/8/7 14:32
     */
    public List<BankUser> getUserListByParams(BankUser bankuser) {
        List<BankUser> users =adminClient.getUserListByParams(bankuser);
        if (users !=null){
            return users;
        }
        return null;
    }
    /**
     * @author: zhanglei
     * @param: [userId]
     * @return:void
     * @description: 修改用户状态(启用)
     * @data: 2019/8/7 14:46
     */
    public void memberStart(Integer userId) {
        adminClient.memberStart(userId);
    }
    /**
     * @author: zhanglei
     * @param: [userId]
     * @return:void
     * @description: 修改用户状态(停用)
     * @data: 2019/8/7 14:46
     */
    public void memberStop(Integer userId) {
        adminClient.memberStop(userId);
    }
    /**
     * @author: zhanglei
     * @param: [userName]
     * @return:void
     * @description: 管理员退出
     * @data: 2019/8/8 11:28
     */
    public void loginOut(String userName) {

        adminClient.loginOut(userName);
    }
    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.ManagerTranscation>
     * @description: 查询用户申请中的提卡信息
     * @data: 2019/8/9 14:59
     */
    public List<ManagerTranscation> getManagerTranscations(Integer pageNum) {
        List<ManagerTranscation> managerTranscations = adminClient.getManagerTranscations(pageNum);
        if (managerTranscations !=null){
            return managerTranscations;
        }
        return null;
    }
    /**
     * @author: zhanglei
     * @param: [transcationId]
     * @return:void
     * @description: 提卡申请(通过)
     * @data: 2019/8/9 15:12
     */
    public void adopt(Integer transcationId) {
        adminClient.adopt(transcationId);
    }
    /**
     * @author: zhanglei
     * @param: [transcationId]
     * @return:void
     * @description: 提卡申请(不通过)
     * @data: 2019/8/9 15:12
     */
    public void NoPassage(Integer transcationId) {
        adminClient.NoPassage(transcationId);
    }
    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 查询异常数
     * @data: 2019/8/6 13:45
     */
    public Integer selectManagerTranscationAll() {
        return Integer.parseInt(adminClient.selectManagerTranscationAll());
    }
    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 查询记录数
     * @data: 2019/8/6 13:44
     */
    public Integer selectTransferRecordAll() {
        String s = adminClient.selectTransferRecordAll();
        return Integer.parseInt(s);
    }
    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 查询会员数
     * @data: 2019/8/6 13:43
     */
    public Integer selectBankUserAll() {
        String s = adminClient.selectBankUserAll();
        return Integer.parseInt(s);
    }

}
