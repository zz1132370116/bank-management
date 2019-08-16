package com.zl.dc.controller;

import com.zl.dc.pojo.BankCard;
import com.zl.dc.service.BankCardService;
import com.zl.dc.service.TransferBulkService;
import com.zl.dc.util.NumberValid;
import com.zl.dc.vo.BaseResult;
import com.zl.dc.vo.NewPayeeVo;
import com.zl.dc.vo.TransferBulkVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * @version: V1.0
 * @author: lu
 * @className: TransferBulkController
 * @description: 批量转账页面控制层
 * @data: 2019/8/15 15:22
 */
@RestController
public class TransferBulkController {

    @Autowired
    private TransferBulkService transferBulkService;

    @Autowired
    private BankCardService bankCardService;


    /**
     * @author: lu
     * @Param List<NewPayeeVo> newPayeeVos:
     * @return: ResponseEntity<BaseResult>
     * @description: 提交批量转账功能
     * @data: 2019/8/15 17:12
     */
    @PostMapping("/submitTransferBulk")
    public ResponseEntity<BaseResult> submitTransferBulk(@RequestBody TransferBulkVo payees) {
        //密码校验

        if (payees.getBankCard() == null) {
            return ResponseEntity.ok(new BaseResult(1, "异常状态请联系管理员"));
        }
        //密码校验
        BankCard selectBankCard = bankCardService.selectBankCardByid(payees.getBankCard().getBankCardId());
        boolean flag = bankCardService.BankCardPasswordCheck(selectBankCard, payees.getBankCard().getBankCardPassword());
        if (!flag) {
            return ResponseEntity.ok(new BaseResult(1, "银行卡密码错误"));
        }
        if (!"100".equals(selectBankCard.getBankCardStatus().toString())) {
            return ResponseEntity.ok(new BaseResult(1, "银行卡状态异常，请查看该卡状态"));
        }
        if (payees.getPayee() == null) {
            return ResponseEntity.ok(new BaseResult(1, "异常状态请联系管理员"));
        }
        //定义批量转账总金额
        BigDecimal allTransferMoney = new BigDecimal(0);
        for (NewPayeeVo npv : payees.getPayee()) {
            //做金额校验
            if (!NumberValid.moneyValid(npv.getMuchMoney().toString())) {
                return ResponseEntity.ok(new BaseResult(1, "转账金额异常，请按照格式输入转账金额"));
            }
            allTransferMoney.add(npv.getMuchMoney());
        }
        if (selectBankCard.getBankCardBalance().compareTo(allTransferMoney) == -1) {
            return ResponseEntity.ok(new BaseResult(1, "余额不足，操作失败"));
        }
        if (selectBankCard.getBankCardTransferLimit() < allTransferMoney.intValue()) {
            return ResponseEntity.ok(new BaseResult(1, "交易被限额，操作失败"));
        }

        //批量转账
        transferBulkService.submitTransferBulk(payees.getPayee(), selectBankCard);

        return ResponseEntity.ok(new BaseResult(0, "批量转账提交成功，请稍后查询"));
    }

}
