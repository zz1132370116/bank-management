package com.zl.dc.controller;

import com.zl.dc.pojo.BankManager;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.ManagerTranscation;
import com.zl.dc.pojo.TransferRecord;
import com.zl.dc.service.AdminService;
import com.zl.dc.vo.BaseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @version: V1.0
 * @author: zhanglei
 * @className: AdminController
 * @description: 管理员
 * @data: 2019/8/6 9:08
 */
@RequestMapping
@RestController
public class AdminController {
    @Resource
    private AdminService adminService;

    /**
     * @author: zhanglei
     * @param: []
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 管理员登录
     * @data: 2019/8/6 9:15
     */
    @PostMapping("/getLogin")
    public ResponseEntity<BaseResult> getLogin(@RequestBody BankManager bankManager) {
        if (!bankManager.getManagerName().equals("") && !bankManager.getManagerPassword().equals("")) {
            //根据service进行操作
            BankManager login = adminService.getLogin(bankManager);

            if (login != null) {
                login.setLoginDate(new Date());
                return ResponseEntity.ok(new BaseResult(0, "成功").append("data", login));
            }
        }
        return ResponseEntity.ok(new BaseResult(1, "失败"));
    }

    /**
     * @author: zhanglei
     * @param: []
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 管理员登录
     * @data: 2019/8/6 9:15
     */
    @GetMapping("/GetUserByRedis")
    public ResponseEntity<BaseResult> GetUserByRedis() {
        BankManager bankManagers = adminService.GetUserByRedis();
        return ResponseEntity.ok(new BaseResult(0, "成功").append("data", bankManagers));
    }

    /**
     * @author: zhanglei
     * @param: []
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 查询会员数
     * @data: 2019/8/6 13:34
     */
    @GetMapping("/GetUserList")
    public ResponseEntity<BaseResult> GetUserList() {
        List<BankUser> bankUsers = adminService.GetUserList();
        return ResponseEntity.ok(new BaseResult(0, "查询成功").append("data", bankUsers.size()));
    }

    /**
     * @author: zhanglei
     * @param: []
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 查询记录数
     * @data: 2019/8/6 13:40
     */
    @GetMapping("/GetRecords")
    public ResponseEntity<BaseResult> GetRecords() {
        List<TransferRecord> transferRecords = adminService.GetRecords();
        return ResponseEntity.ok(new BaseResult(0, "查询成功").append("data", transferRecords.size()));
    }

    /**
     * @author: zhanglei
     * @param: []
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 查询异常数
     * @data: 2019/8/6 13:40
     */
    @GetMapping("/GetAbnormals")
    public ResponseEntity<BaseResult> GetAbnormals() {
        List<ManagerTranscation> managerTranscations = adminService.GetAbnormals();
        return ResponseEntity.ok(new BaseResult(0, "查询成功").append("data", managerTranscations.size()));
    }

    /**
     * @author: zhanglei
     * @param: []
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 查询所有订单
     * @data: 2019/8/6 18:57
     */
    @PostMapping("/getTransferRecords")
    public ResponseEntity<BaseResult> getTransferRecords(@RequestBody TransferRecord transferRecord) {

            //校验是否有条件
            if ((transferRecord.getIdCard()!=null && !transferRecord.getIdCard().equals("")) || (transferRecord.getStartDate() !=null && !transferRecord.getStartDate().equals("")) || (transferRecord.getEndDate()!=null && !transferRecord.getEndDate().equals(""))) {
                //进行条件查询
                List<TransferRecord> transferRecords = adminService.getRecordsByParams(transferRecord);
                return ResponseEntity.ok(new BaseResult(0, "条件查询成功").append("data", transferRecords));
            }

        //查询所有记录
        List<TransferRecord> transferRecords = adminService.GetRecords();
        return ResponseEntity.ok(new BaseResult(0, "查询成功").append("data", transferRecords));

    }
    /**
     * @author: zhanglei
     * @param: [bankUser]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 查询所有用户(带条件)
     * @data: 2019/8/7 14:29
     */
    @PostMapping("/getBankUserList")
    public ResponseEntity<BaseResult> getBankUserList(@RequestBody BankUser bankUser){
        //校验条件
        if ((bankUser.getUserName()!=null && !bankUser.getUserName().equals("")) ||(bankUser.getIdCard()!=null && !bankUser.getIdCard().equals(""))){
           List<BankUser> users = adminService.getUserListByParams(bankUser);
            return ResponseEntity.ok(new BaseResult(0,"条件查询成功").append("data",users));
        }
        //查询所有
        List<BankUser> users = adminService.GetUserList();
        return ResponseEntity.ok(new BaseResult(0,"查询成功").append("data",users));
    }
    /**
     * @author: zhanglei
     * @param: [userId]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 修改用户状态(启用)
     * @data: 2019/8/7 14:49
     */
    @GetMapping("/memberStart/{userId}")
    public ResponseEntity<BaseResult> memberStart(@PathVariable("userId")Integer userId){
        adminService.memberStart(userId);
    return ResponseEntity.ok(new BaseResult(0,"启用成功"));
    }
    /**
     * @author: zhanglei
     * @param: [userId]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 修改用户状态(停用)
     * @data: 2019/8/7 14:49
     */
    @GetMapping("/memberStop/{userId}")
    public ResponseEntity<BaseResult> memberStop(@PathVariable("userId")Integer userId){
        adminService.memberStop(userId);
        return ResponseEntity.ok(new BaseResult(0,"停用成功"));
    }
    /**
     * @author: zhanglei
     * @param: []
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 功能描述
     * @data: 2019/8/8 11:22
     */
    @PostMapping("/loginOut/{userName}")
    public ResponseEntity<BaseResult>loginOut(@PathVariable("userName")String userName){
        adminService.loginOut(userName);
        return ResponseEntity.ok(new BaseResult(0,"成功"));
    }


}
