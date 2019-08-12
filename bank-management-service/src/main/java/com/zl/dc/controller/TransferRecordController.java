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
 * @author: Redsheep
 * @className: TransferRecordController
 * @description: 转账记录控制层
 * @data: 2019/8/6 19:14
 */
@RestController
@RequestMapping
public class TransferRecordController {
    @Resource
    private TransferRecordService transferRecordService;


}
