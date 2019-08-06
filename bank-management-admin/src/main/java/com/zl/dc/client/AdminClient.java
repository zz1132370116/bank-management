package com.zl.dc.client;

import com.zl.dc.pojo.BankManager;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.ManagerTranscation;
import com.zl.dc.pojo.TransferRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

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

    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 查询会员数
     * @data: 2019/8/6 13:43
     */
    @GetMapping("/GetUserList")
    List<BankUser> GetUserList();

    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 查询记录数
     * @data: 2019/8/6 13:44
     */
    @GetMapping("/GetRecords")
    List<TransferRecord> GetRecords();

    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 查询异常数
     * @data: 2019/8/6 13:45
     */
    @GetMapping("/GetAbnormals")
    List<ManagerTranscation> GetAbnormals();
    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 通过redis获取用户登录信息
     * @data: 2019/8/6 15:21
     */
    @GetMapping("/GetUserByRedis")
    ResponseEntity<BankManager> GetUserByRedis();

    /**
     * @author: zhanglei
     * @param: [idCard, startDate, endDate]
     * @return:java.util.List<com.zl.dc.pojo.TransferRecord>
     * @description: 条件查询记录
     * @data: 2019/8/6 19:11
     */
    @PostMapping("/getRecordsByParams")
    List<TransferRecord> getRecordsByParams(@RequestParam("idCard") String idCard,@RequestParam("startDate") Date startDate, @RequestParam("endDate")Date endDate);
}
