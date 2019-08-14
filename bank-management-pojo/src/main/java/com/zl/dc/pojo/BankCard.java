package com.zl.dc.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @version: V1.0
 * @author: redsheep
 * @className: BankCard
 * @description: 银行卡实体类
 * @data: 2019/8/12 14:42
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
     * 9999 **** **** ****
     */
    @Column(name = "bank_card_number")
    private String bankCardNumber;
    /**
     * 银行卡密码密文
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
     * 普通卡 ￥500,000
     * 钻石卡 ￥1,000,000
     * 黑卡 ￥10,000,000
     * 企业卡 ￥100,000,000
     */
    @Column(name = "bank_card_type")
    private String bankCardType;
    /**
     * 银行卡状态
     * 100 正常
     * 101 挂失
     * 102 冻结
     * 103 注销
     */
    @Column(name = "bank_card_status")
    private Byte bankCardStatus;
    /**
     * 银行卡预留手机号码
     */
    @Column(name = "bank_card_phone")
    private String bankCardPhone;
    /**
     * 当日交易限额
     * 默认根据银行卡类型决定最大交易限额
     * 用户可更改该交易限额
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

    /**
     * 银行
     */
    @Transient
    private String bank;
    /**
    *备注
    */
    @Transient
    private String transcationMsg;
    /**
     *验证码
     */
    @Transient
    private String code;


     }
