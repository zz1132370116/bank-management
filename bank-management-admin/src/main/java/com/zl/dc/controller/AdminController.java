package com.zl.dc.controller;

import com.zl.dc.service.AdminService;
import com.zl.dc.vo.BaseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
     @PostMapping("/getLogin")
    public ResponseEntity<BaseResult> getLogin(){
         return ResponseEntity.ok(new BaseResult(0,"成功"));
     }
}
