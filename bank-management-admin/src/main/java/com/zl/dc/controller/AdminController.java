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
                return ResponseEntity.ok(new BaseResult(0, "成功").append("data", login));
            }
        }
        return ResponseEntity.ok(new BaseResult(1, "失败"));
    }

    /**
     * @author: zhanglei
     * @param: []
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 查询会员数
     * @data: 2019/8/6 13:34
     */
    @GetMapping("/GetUserList")
    public ResponseEntity<BaseResult> GetUserList(){
        List<BankUser> bankUsers = adminService.GetUserList();
        return ResponseEntity.ok(new BaseResult(0,"查询成功").append("data",bankUsers.size()));
    }
    /**
     * @author: zhanglei
     * @param: []
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 查询记录数
     * @data: 2019/8/6 13:40
     */
    @GetMapping("/GetRecords")
    public ResponseEntity<BaseResult>GetRecords(){
        List<TransferRecord> transferRecords = adminService.GetRecords();
        return ResponseEntity.ok(new BaseResult(0,"查询成功").append("data",transferRecords.size()));
    }
    /**
     * @author: zhanglei
     * @param: []
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 查询异常数
     * @data: 2019/8/6 13:40
     */
    @GetMapping("/GetAbnormals")
    public ResponseEntity<BaseResult>GetAbnormals(){
        List<ManagerTranscation> managerTranscations = adminService.GetAbnormals();
        return ResponseEntity.ok(new BaseResult(0,"查询成功").append("data",managerTranscations.size()));
    }
}
