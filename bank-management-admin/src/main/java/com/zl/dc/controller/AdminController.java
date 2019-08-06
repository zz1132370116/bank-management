package com.zl.dc.controller;

import com.zl.dc.pojo.BankManager;
import com.zl.dc.service.AdminService;
import com.zl.dc.vo.BaseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
}
