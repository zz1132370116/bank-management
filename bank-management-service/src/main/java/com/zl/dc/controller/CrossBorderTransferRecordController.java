package com.zl.dc.controller;

import com.zl.dc.pojo.CrossBorderTransferRecord;
import com.zl.dc.service.CrossBorderTransferRecordService;
import com.zl.dc.vo.BaseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    @Resource
    private CrossBorderTransferRecordService crossBorderTransferRecordService;

    /**
     * @author: zhanglei
     * @param: [crossBorderTransferRecord]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 根据条件查询汇率
     * @data: 2019/8/11 9:48
     */
    @PostMapping("/getExchangeRate")
    public ResponseEntity<BaseResult> getExchangeRate(@RequestBody CrossBorderTransferRecord crossBorderTransferRecord) {

        if (crossBorderTransferRecord != null) {
            CrossBorderTransferRecord crossBorderTransferRecord1 = crossBorderTransferRecordService.getExchangeRate(crossBorderTransferRecord);
            return ResponseEntity.ok(new BaseResult(0, "成功").append("data", crossBorderTransferRecord1));
        }
        return ResponseEntity.ok(new BaseResult(1, "失败"));
    }
    /**
     * @author: zhanglei
     * @param: [crossBorderTransferRecord]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 根据人民币查询外币
     * @data: 2019/8/11 9:48
     */
    @GetMapping("/getExchangeRatePrice/{price}/{type}")
    public ResponseEntity<BaseResult> getExchangeRatePrice(@PathVariable("price")String price,@PathVariable("type") String type) {

        if (price != "" && type !="") {
            CrossBorderTransferRecord crossBorderTransferRecord1 = crossBorderTransferRecordService.getExchangeRatePrice(price,type);
            if (crossBorderTransferRecord1 != null) {
                return ResponseEntity.ok(new BaseResult(0, "成功").append("data", crossBorderTransferRecord1));
            }
            return ResponseEntity.ok(new BaseResult(1, "请重试"));
        }
        return ResponseEntity.ok(new BaseResult(1, "失败"));
    }
    /**
     * @author: zhanglei
     * @param: [price, type]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 根据外币查询人民币
     * @data: 2019/8/13 9:41
     */
    @GetMapping("/getExchangeRateCNY/{price}/{type}")
    public ResponseEntity<BaseResult> getExchangeRateCNY(@PathVariable("price")String price,@PathVariable("type") String type) {
        if (price != "" && type !="") {
            CrossBorderTransferRecord crossBorderTransferRecord1 = crossBorderTransferRecordService.getExchangeRateCNY(price,type);
            if (crossBorderTransferRecord1 != null) {
                return ResponseEntity.ok(new BaseResult(0, "成功").append("data", crossBorderTransferRecord1));
            }
            return ResponseEntity.ok(new BaseResult(1, "请重试"));
        }
        return ResponseEntity.ok(new BaseResult(1, "失败"));
    }
    @PostMapping("/CrossBorderTransfer")
    public ResponseEntity<BaseResult> CrossBorderTransfer(@RequestBody CrossBorderTransferRecord crossBorderTransferRecord){
        if (crossBorderTransferRecord != null){
            crossBorderTransferRecordService.CrossBorderTransfer(crossBorderTransferRecord);
        }
    }
}
