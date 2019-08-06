package com.zl.dc.controller;

import com.zl.dc.pojo.BankManager;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.ManagerTranscation;
import com.zl.dc.pojo.TransferRecord;
import com.zl.dc.service.BankManagerService;
import com.zl.dc.vo.BaseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

     @PostMapping("/getLogin")
     public ResponseEntity<BankManager> getLogin(@RequestParam(value = "managerName", required = false) String managerName,
                                                 @RequestParam(value = "managerPassword", required = false) String managerPassword){
         BankManager bankManager1 =bankManagerService.getLogin(managerName);
         if (bankManager1 !=null){
             if (managerPassword.equals(bankManager1.getManagerPassword())){
                 return ResponseEntity.ok(bankManager1);
             }else{
                 return ResponseEntity.ok(null);
             }
         }
         return ResponseEntity.ok(null);
     }




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
