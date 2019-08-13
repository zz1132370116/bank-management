package com.zl.dc.controller;

import com.alibaba.fastjson.JSON;
import com.zl.dc.pojo.BankCard;
import com.zl.dc.pojo.SubordinateBank;
import com.zl.dc.pojo.TransferRecord;
import com.zl.dc.service.BankCardService;
import com.zl.dc.service.SubordinateBankService;
import com.zl.dc.service.TransferRecordService;
import com.zl.dc.vo.BaseResult;
import com.zl.dc.vo.TransferValueVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


/**
 * @version: V1.0
 * @author: lu
 * @className: 单次转账控制层
 * @description:
 * @data: 2019/8/11 16:16
 */
@RestController
public class TransferController {

    @Autowired
    private SubordinateBankService subordinateBankService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private BankCardService bankCardService;

    @Autowired
    private TransferRecordService transferRecordService;

    /**
     * @author: lu
     * @param: []
     * @return: org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 用于查询出所有所属银行的方法
     * @data: 2019/8/11 17:38
     */
    @GetMapping("getAllSubordinateBank")
    public ResponseEntity<BaseResult> getAllSubordinateBank() {
        //从Redis中查询所属银行
        String SubordinateBankJson = stringRedisTemplate.opsForValue().get("subordinateBank");
        //如果redis里面有所属银行
        if (SubordinateBankJson != null) {
            //将查询出的所属银行json放入返回模型。添加返回成功字段，返回
            return ResponseEntity.ok(new BaseResult(0, "成功").append("data", SubordinateBankJson));
        }
        //如果redis里面没有所属银行
//        查询出所有的所属银行
        List<SubordinateBank> subordinateBanks = subordinateBankService.getAllSubordinateBank();
//        放入redis并且返回
        stringRedisTemplate.opsForValue().set("subordinateBank", JSON.toJSONString(subordinateBanks));
        //将查询出的所属银行放入返回模型。添加返回成功字段，返回
        return ResponseEntity.ok(new BaseResult(0, "成功").append("data", subordinateBanks));
    }

    /**
     * @author: lu
     * @param: * TransferValueVo
     * @return: * ResponseEntity<BaseResult>
     * @description: 单次转账功能
     * @data: 2019/8/12 14:13
     */
    @PostMapping("/verifyBankCardForVo")
    public ResponseEntity<BaseResult> verifyBankCardForVo(@RequestBody TransferValueVo transferValueVo) {
        //查询银行卡
        BankCard bankCard = bankCardService.verifyBankCardForVo(transferValueVo);
        if (bankCard == null) {
            return ResponseEntity.ok(new BaseResult(1, "密码错误"));
        }
        if (bankCard.getBankCardBalance().compareTo(transferValueVo.getMuchMoney()) == -1) {
            return ResponseEntity.ok(new BaseResult(1, "余额不足，操作失败"));
        }

//        添加转账记录
        TransferRecord transferRecord = transferRecordService.addTransferRecordforTransferValueVo(transferValueVo);
        if (transferRecord == null) {
            return ResponseEntity.ok(new BaseResult(1, "操作异常请通知管理员"));
        }
        //操作银行卡扣款 转账状态
        boolean transferStatus = bankCardService.bankCardTransferBusines(transferValueVo);
//          如果转账失败
        if (!transferStatus) {
            boolean transferFailedStatus = transferRecordService.transferFailedOperation(transferRecord);
            if (transferFailedStatus) {
                return ResponseEntity.ok(new BaseResult(1, "转账失败"));
            } else {
                return ResponseEntity.ok(new BaseResult(1, "操作异常请通知管理员"));
            }
        }
        //如果转账成功
        boolean transferSuccessfulStatus = transferRecordService.transferSuccessfulOperation(transferRecord);
        if (transferSuccessfulStatus) {
            return ResponseEntity.ok(new BaseResult(1, "转账成功"));
        } else {
            return ResponseEntity.ok(new BaseResult(1, "操作异常请通知管理员"));
        }
    }
}
