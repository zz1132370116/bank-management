package com.zl.dc.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;

/**
 * @author 舌頭會游泳
 * @Auther: 舌頭會游泳
 * @Date: 2019/8/3 15:43
 * @Description:
 */
@Table(name = "transfer_record")
@Data
@ToString
public class TransferRecord {
    /**
     * 转账单号UUID
     */
    @Id
    @Column(name = "transfer_record_uuid")
    private String transferRecordUuid;
    /**
     * 转账金额
     */
    @Column(name = "transfer_record_amount")
    private Double transferRecordAmount;
    /**
     * 转账时间
     */
    @Column(name = "transfer_record_time")
    private Timestamp transferRecordTime;
    /**
     * 订单状态
     */
    @Column(name = "transaction_status")
    private Integer transactionStatus;
    /**
     * 转出银行卡id
     */
    @Column(name = "bank_out_card_id")
    private Integer bankOutCardId;
    /**
     * 转入银行卡id
     */
    @Column(name = "bank_in_card_id")
    private Integer bankInCardId;
    /**
     * 备注
     */
    @Column(name = "remarks")
    private String remarks;
    /**
     * 到账时间
     */
    @Column(name = "payment_date")
    private Timestamp paymentDate;


    /**
     * 转出卡
     * @return
     */
    @Transient
    private BankCard outBankCard;

    /**
     * 转入卡
     * @return
     */
    @Transient
    private BankCard inBankCard;


}
