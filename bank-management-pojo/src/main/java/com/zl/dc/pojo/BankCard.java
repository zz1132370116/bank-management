package com.zl.dc.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @version: V1.1
 * @author: lu
 * @className: 银行卡
 * @description:
 * @data: 2019/8/6 9:32
 */
@Data
@ToString
@Table(name = "bank_card")
public class BankCard {
    /**
     * 银行卡id
     */
    @Id
    @Column(name = "bank_card_id")
    private Integer bankCardId;
    /**
     * 银行卡卡号
     */
    @Column(name = "bank_card_number")
    private String bankCardNumber;
    //    银行卡密码加密
    /**
     * 所属用户
     */
    @Column(name = "bank_card_password")
    private String bankCardPassword;
    /**
     * 所属用户
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 银行卡余额
     */
    @Column(name = "bank_card_balance")
    private BigDecimal bankCardBalance;

    /**
     * 银行卡类型
     * 100为用户普通卡
     * 110为用户钻石卡
     * 120为用户黑卡
     * 130位企业用卡
     */
    @Column(name = "bank_card_type")
    private String bankCardType;

    /**
     * 银行卡状态
     * 100 为正常时用状态
     * 110 为申请状态
     * 120 为冻结状态
     */
    @Column(name = "bank_card_status")
    private byte bankCardStatus;
    /**
     * 银行卡预留手机号码
     */
    @Column(name = "bank_card_phone")
    private String bankCardPhone;
    /**
     * 交易限额
     */
    @Column(name = "bank_card_transfer_limit")
    private Integer bankCardTransferLimit;
    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModified;


}
