package com.zl.dc.controller;

import com.zl.dc.pojo.BankCard;
import com.zl.dc.pojo.FundCollectionPlan;
import com.zl.dc.pojo.TransferRecord;
import com.zl.dc.service.BankCardService;
import com.zl.dc.service.FundCollectionService;
import com.zl.dc.service.TransferRecordService;
import com.zl.dc.vo.AuthVO;
import com.zl.dc.vo.BaseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.net.www.protocol.http.AuthenticationHeader;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version: V1.0
 * @author: redsheep
 * @className: FundCollectionController
 * @description: 资金归集controller
 * @data: 2019/8/16 15:42
 */
@RestController
@RequestMapping
public class FundCollectionController {

    @Resource
    FundCollectionService fundCollectionService;
    @Resource
    TransferRecordService transferRecordService;
    @Resource
    BankCardService bankCardService;

    /**
     * @author: Redsheep
     * @Param userId
     * @return: org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 根据用户id所有的归集计划
     * @data: 2019/8/16 15:34
     */
    @GetMapping("/getFundCollectionPlanList/{userId}")
    public ResponseEntity<BaseResult> getFundCollectionPlanList(@PathVariable("userId") Integer userId) {
        if (userId == null) {
            return ResponseEntity.ok(new BaseResult(1, "参数错误"));
        }
        List<FundCollectionPlan> fundCollectionPlans = fundCollectionService.getFundCollectionPlanList(userId);
        if (fundCollectionPlans == null) {
            return ResponseEntity.ok(new BaseResult(0, "没有查到任何归集计划"));
        }
        return ResponseEntity.ok(new BaseResult(0, "查询成功").append("data", fundCollectionPlans));
    }

    /**
     * @author: Redsheep
     * @Param fundCollectionPlan
     * @return: org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 添加归集计划
     * @data: 2019/8/16 15:35
     */
    @PostMapping("/addFundCollectionPlan")
    public ResponseEntity<BaseResult> addFundCollectionPlan(@RequestBody FundCollectionPlan fundCollectionPlan) {
        if (fundCollectionPlan == null
                || fundCollectionPlan.getBankInCardId() == null
                || fundCollectionPlan.getBankOutCardId() == null
                || fundCollectionPlan.getUserId() == null
                || fundCollectionPlan.getCollectionAmount() == null
                || fundCollectionPlan.getCollectionMonth() == null
                || fundCollectionPlan.getCollectionDay() == null
                || fundCollectionPlan.getPlanName() == null) {
            return ResponseEntity.ok(new BaseResult(1, "参数错误"));
        }
        if (fundCollectionService.addFundCollectionPlan(fundCollectionPlan)) {
            return ResponseEntity.ok(new BaseResult(0, "添加成功"));
        }
        return ResponseEntity.ok(new BaseResult(1, "添加错误"));
    }

    /**
     * @author: Redsheep
     * @Param authVO
     * @return: org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 手动终止一个归集计划
     * @data: 2019/8/16 15:35
     */
    @PostMapping("/shutdownFundCollectionPlan")
    public ResponseEntity<BaseResult> shutdownFundCollectionPlan(@RequestBody AuthVO authVO) {
        if (authVO == null
                || authVO.getUserId() == null
                || authVO.getBankCardId() == null
                || authVO.getPassword() == null
                || authVO.getData() == null) {
            return ResponseEntity.ok(new BaseResult(1, "参数错误"));
        }
        if (!bankCardService.verifyBankCardPassword(authVO.getBankCardId(),authVO.getPassword(),authVO.getUserId())) {
            return ResponseEntity.ok(new BaseResult(1, "密码错误"));
        }
        if (authVO.getData() instanceof Integer) {
            if (fundCollectionService.shutdownFundCollectionPlan((Integer) authVO.getData())) {
                return ResponseEntity.ok(new BaseResult(0, "终止成功"));
            }
        }
        return ResponseEntity.ok(new BaseResult(1, "终止失败"));
    }

    /**
     * @author: Redsheep
     * @Param userId
     * @return: org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 查看某个归集计划的记录
     * @data: 2019/8/16 15:35
     */
    @GetMapping("/getFundCollectionRecordList/{planId}")
    public ResponseEntity<BaseResult> getFundCollectionRecordList(@PathVariable("planId") Integer planId) {
        if(planId==null){
            return ResponseEntity.ok(new BaseResult(1, "密码错误"));
        }
        return null;
    }
}
