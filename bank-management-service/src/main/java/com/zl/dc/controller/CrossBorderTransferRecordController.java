package com.zl.dc.controller;

import com.zl.dc.service.CrossBorderTransferRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
  * @version: V1.0
  * @author: zhanglei
  * @className: CrossBorderTransferRecordController
  * @description: 跨境转账控制层
  * @data: 2019/8/10 16:43
  */
 @RestController
 @RequestMapping
public class CrossBorderTransferRecordController {

     private CrossBorderTransferRecordService crossBorderTransferRecordService;

}
