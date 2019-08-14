package com.zl.dc.controller;

import com.zl.dc.pojo.TransferRecord;
import com.zl.dc.service.TransferRecordService;
import com.zl.dc.vo.BaseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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


    @GetMapping("/getTransferRecordList")
    public ResponseEntity<BaseResult> getTransferRecordList(@RequestParam("page") Integer page,
                                                            @RequestParam("month") Integer month,
                                                            @RequestParam("userId") Integer userId,
                                                            @RequestParam("bankCardId") Integer bankCardId) {
        // 参数判空
        if (page == null || userId == null || month == null) {
            return ResponseEntity.ok(new BaseResult(1, "参数错误"));
        }
        List<TransferRecord> transferRecordList = transferRecordService.getTransferRecordList(page, month, userId, bankCardId);
        if (transferRecordList == null) {
            return ResponseEntity.ok(new BaseResult(1, "查询不到数据"));
        } else {
            return ResponseEntity.ok(new BaseResult(0, "查询成功").append("data", transferRecordList));
        }
    }

}
