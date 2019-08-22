package com.zl.dc.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zl.dc.pojo.BankCard;
import com.zl.dc.pojo.TransferRecord;
import com.zl.dc.vo.BaseResult;
import com.zl.dc.vo.NewPayeeVo;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @version: V1.0
 * @author: lu
 * @className: TransferBulkService
 * @description: 批量转账业务层
 * @data: 2019/8/15 15:22
 */
@Service
public class TransferBulkService {
    @Resource
    private TransferRecord transferRecord;
    @Resource
    private BankCardService bankCardService;
    @Resource
    private TransferRecordService transferRecordService;

    /**
     * @author: lu
     * @Param List<NewPayeeVo> newPayeeVos:
     * @return: void
     * @description: 提交批量转账功能
     * @data: 2019/8/15 17:12
     */
    public void submitTransferBulk(List<NewPayeeVo> newPayeeVos, BankCard bankCard) {
        transferRecord.setTransferStatus(Byte.parseByte("100"));
        transferRecord.setTransferType(Byte.parseByte("101"));
        transferRecord.setUserId(bankCard.getUserId());
        transferRecord.setBankOutCard(bankCard.getBankCardNumber());


        for (NewPayeeVo newPayeeVo : newPayeeVos) {
            transferRecord.setTransferRecordUuid(UUID.randomUUID().toString().replaceAll("-", ""));
            transferRecord.setInCardUserName(newPayeeVo.getPayeeName());
            transferRecord.setBankInIdentification(newPayeeVo.getPayeeBankIdentification());
            transferRecord.setBankInCard(newPayeeVo.getPayeeBankCard());
            transferRecord.setGmtModified(new Date());
            transferRecord.setGmtCreate(new Date());
            transferRecord.setTransferRecordAmount(newPayeeVo.getMuchMoney());

            //新增转账记录
            transferRecordService.insertTransferRecord(transferRecord);
            TransferRecord selectTransferRecord = transferRecordService.selectTransferRecordByUuid(this.transferRecord.getTransferRecordUuid());
            //做收款人姓名校验，如果失败，将转账设定为失败
            boolean nameCheck = bankCardService.checkNameAndBankCard(newPayeeVo.getPayeeName(), newPayeeVo.getPayeeBankCard());
            if (nameCheck) {
                //扣款
                boolean transferStatus = bankCardService.bankCardTransferBusines(bankCard.getBankCardId(), newPayeeVo.getPayeeBankCard(), newPayeeVo.getMuchMoney());
                if (transferStatus) {
                    //                转账记录添加成功操作
                    transferRecordService.transferSuccessfulOperation(selectTransferRecord);
                } else {
                    //                转账记录添加失败操作
                    transferRecordService.transferFailedOperation(selectTransferRecord);
                }
            } else {
                //                转账记录添加失败操作
                transferRecordService.transferFailedOperation(selectTransferRecord);
            }
        }
    }


}
