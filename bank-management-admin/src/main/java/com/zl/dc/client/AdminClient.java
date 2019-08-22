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
    @GetMapping("/selectBankUserAll")
   String selectBankUserAll();

    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 查询转账记录数
     * @data: 2019/8/6 13:44
     */
    @GetMapping("/selectTransferRecordAll")
   String  selectTransferRecordAll();

    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 查询事务数
     * @data: 2019/8/6 13:45
     */
    @GetMapping("/selectManagerTranscationAll")
   String selectManagerTranscationAll();
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
    List<TransferRecord> getRecordsByParams(@RequestBody TransferRecord transferRecord);
    /**
     * @author: zhanglei
     * @param: [userName, idCard]
     * @return:java.util.List<com.zl.dc.pojo.BankUser>
     * @description: 通过条件查询用户
     * @data: 2019/8/7 14:32
     */
    @PostMapping("/getUserListByParams")
    List<BankUser> getUserListByParams(@RequestBody BankUser bankUser);

    /**
     * @author: zhanglei
     * @param: [userId]
     * @return:void
     * @description: 修改用户状态(启用)
     * @data: 2019/8/7 14:46
     */
    @GetMapping("/memberStart")
    void memberStart(@RequestParam("userId") Integer userId);

    /**
     * @author: zhanglei
     * @param: [userId]
     * @return:void
     * @description: 修改用户状态(停用)
     * @data: 2019/8/7 14:46
     */
    @GetMapping("/memberStop")
    void memberStop(@RequestParam("userId") Integer userId);
    /**
     * @author: zhanglei
     * @param: [userName]
     * @return:void
     * @description: 管理员退出
     * @data: 2019/8/8 11:28
     */
    @GetMapping("/loginOut")
    void loginOut(@RequestParam("userName") String userName);
    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.ManagerTranscation>
     * @description: 查询用户申请中的提额信息
     * @data: 2019/8/9 14:59
     */
    @GetMapping("/getManagerTranscations")
    List<ManagerTranscation> getManagerTranscations(@RequestParam("pageNum") Integer pageNum);

    /**
     * @author: zhanglei
     * @param: [transcationId]
     * @return:void
     * @description: 提额申请(通过)
     * @data: 2019/8/9 15:12
     */
    @GetMapping("/adopt")
    void adopt(@RequestParam("transcationId") Integer transcationId);
    /**
     * @author: zhanglei
     * @param: [transcationId]
     * @return:void
     * @description: 提额申请(通过)
     * @data: 2019/8/9 15:12
     */
    @GetMapping("/NoPassage")
    void NoPassage(@RequestParam("transcationId") Integer transcationId);
    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 查询会员数
     * @data: 2019/8/6 13:43
     */
    @GetMapping("/GetUserList")
    List<BankUser> GetUserList(@RequestParam("pageNum") Integer pageNum);

    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 查询记录数
     * @data: 2019/8/6 13:44
     */
    @GetMapping("/GetRecords")
    List<TransferRecord> GetRecords(@RequestParam("pageNum") Integer pageNum);

    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 查询异常数
     * @data: 2019/8/6 13:45
     */
    @GetMapping("/GetAbnormals")
    List<ManagerTranscation> GetAbnormals(@RequestParam("pageNum") Integer pageNum);
}
