package com.zl.dc.controller;

import com.zl.dc.pojo.BankManager;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.ManagerTranscation;
import com.zl.dc.pojo.TransferRecord;
import com.zl.dc.service.BankManagerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
  * @version: V1.0
  * @author: zhanglei
  * @className: BankManagerController
  * @description: 管理员
  * @data: 2019/8/6 13:48
  */
 @RestController
 @RequestMapping
public class BankManagerController {
     @Resource
    private BankManagerService bankManagerService;

    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 查询管理员会员数
     * @data: 2019/8/6 13:53
     */
     @GetMapping("/GetUserList")
    public List<BankUser> GetUserList(){
         List<BankUser> list= bankManagerService. GetUserList();
         return list;
     }
    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 查询管理员记录数
     * @data: 2019/8/6 13:53
     */
    @GetMapping("/GetRecords")
    public List<TransferRecord> GetRecords(){
        List<TransferRecord> list= bankManagerService. GetRecords();
        return list;
    }
    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 查询管理员异常数
     * @data: 2019/8/6 13:53
     */
    @GetMapping("/GetAbnormals")
    public List<ManagerTranscation> GetAbnormals(){
        List<ManagerTranscation> list= bankManagerService. GetAbnormals();
        return list;
    }
}
