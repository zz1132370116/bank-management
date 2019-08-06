package com.zl.dc.controller;

import com.alibaba.fastjson.JSON;
import com.zl.dc.pojo.BankManager;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.ManagerTranscation;
import com.zl.dc.pojo.TransferRecord;
import com.zl.dc.service.BankManagerService;
import com.zl.dc.vo.BaseResult;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Resource
    private StringRedisTemplate redisTemplate;

    /**
     * @author: zhanglei
     * @param: [managerName, managerPassword]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.pojo.BankManager>
     * @description: 管理员登录
     * @data: 2019/8/6 15:15
     */
     @PostMapping("/getLogin")
     public ResponseEntity<BankManager> getLogin(@RequestParam(value = "managerName", required = false) String managerName,
                                                 @RequestParam(value = "managerPassword", required = false) String managerPassword){
         BankManager bankManager1 =bankManagerService.getLogin(managerName);
         if (bankManager1 !=null){
             if (managerPassword.equals(bankManager1.getManagerPassword())){
                 redisTemplate.opsForValue().set(bankManager1.getManagerName(), bankManager1.getManagerPassword());
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
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.pojo.BankManager>
     * @description: 通过redis获取当前管理员登录信息
     * @data: 2019/8/6 15:30
     */
@GetMapping("/GetUserByRedis")
public ResponseEntity<BankManager> GetUserByRedis(){
    List<BankManager> bankManagers =bankManagerService.GetUserByRedis();
    for (BankManager bankManager : bankManagers) {
        String s = redisTemplate.opsForValue().get(bankManager.getManagerName());
        if (!s .equals("")){
            return ResponseEntity.ok(bankManager);
        }
    }
    return null;
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
