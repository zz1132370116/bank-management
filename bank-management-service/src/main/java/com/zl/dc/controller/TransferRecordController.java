package com.zl.dc.controller;

import com.zl.dc.pojo.TransferRecord;
import com.zl.dc.service.TransferRecordService;
import com.zl.dc.vo.BaseResult;
import com.zl.dc.vo.PageBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


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

    @PostMapping("/getTransferRecord")
    public ResponseEntity<BaseResult> getTransferRecord(@RequestBody PageBean<TransferRecord> pageBean) {
        return ResponseEntity.ok(new BaseResult(1, "查询成功").append("data", pageBean.getList()));
    }
}
