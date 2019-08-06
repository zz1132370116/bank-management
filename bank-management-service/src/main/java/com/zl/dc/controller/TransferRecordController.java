package com.zl.dc.controller;

import com.zl.dc.pojo.TransferRecord;
import com.zl.dc.service.TransferRecordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
  * @version: V1.0
  * @author: zhanglei
  * @className: TransferRecordController
  * @description: 转账记录控制层
  * @data: 2019/8/6 19:14
  */
 @RestController
 @RequestMapping
public class TransferRecordController {
     @Resource
    private TransferRecordService transferRecordService;
    /**
     * @author: zhanglei
     * @param: [idCard, startDate, endDate]
     * @return:java.util.List<com.zl.dc.pojo.TransferRecord>
     * @description: 条件查询记录
     * @data: 2019/8/6 19:18
     */
     @PostMapping("/getRecordsByParams")
    public List<TransferRecord> getRecordsByParams(@RequestParam("idCard") String idCard, @RequestParam("startDate")Date startDate,@RequestParam("endDate")Date endDate){
         return transferRecordService.getRecordsByParams(idCard,startDate,endDate);

     }
 }
