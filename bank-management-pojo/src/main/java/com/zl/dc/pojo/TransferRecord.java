package com.zl.dc.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @version: V1.0
 * @author: redsheep
 * @className: TransferRecord
 * @description: 转账记录表
 * @data: 2019/8/12 9:53
 */
@Data
@ToString
@Component
@Table(name = "transfer_record")
public class TransferRecord {
    /**
     * 主键
     */
    @Id
    @Column(name = "transfer_record_id")
    private Integer transferRecordId;
    /**
     * 转账记录流水号
     */
    @Column(name = "transfer_record_uuid")
    private String transferRecordUuid;
    /**
     * 交易额
     */
    @Column(name = "transfer_record_amount")
    private BigDecimal transferRecordAmount;
    /**
     * 订单时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "transfer_record_time")
    private Date transferRecordTime;
    /**
     * 转账备注
     */
    @Column(name = "transfer_note")
    private String transferNote;
    /**
     * 交易状态
     * 100 交易中
     * 101 成功
     * 102 失败
     */
    @Column(name = "transfer_status")
    private Byte transferStatus;
    /**
     * 转账类型
     * 100 单次转账
     * 101 批量转账
     * 102 主动收款
     * 103 跨境转账
     * 104 手机转账
     * 105 企业转个人
     * 106 个人转企业
     * 107 资金归集
     */
    @Column(name = "transfer_type")
    private Byte transferType;
    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;
    /**
     * 用户自己的卡
     */
    @Column(name = "bank_out_card")
    private String bankOutCard;
    /**
     * 收款人姓名
     */
    @Column(name = "in_card_user_name")
    private String inCardUserName;
    /**
     * 转入卡的银行标识码
     */
    @Column(name = "bank_in_identification")
    private String bankInIdentification;
    /**
     * 转入卡
     */
    @Column(name = "bank_in_card")
    private String bankInCard;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "gmt_create")
    private Date gmtCreate;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "gmt_modified")
    private Date gmtModified;

    /**
     * 自定义属性
     */

    /**
     * 用户身份证
     */
    @Transient
    private String idCard;
    /**
     * 银行卡id
     */
    @Transient
    private int bankOutCardId;
    /**
     * 转出卡银行
     */
    @Transient
    private String bankOutCardName;
    /**
     * 转入卡银行
     */
    @Transient
    private String bankInCardName;
    /**
     * 用户姓名
     */
    @Transient
    private String userName;
    /**
     * 查询时间
     */
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    /**
     * 转账状态
     */
    @Transient
    private String transferStringType;
    /**
     * 转账类型
     */
    @Transient
    private String transferStringStatus;
    //页码
    @Transient
    private Integer pageNum;
    //总页数
    @Transient
    private Integer totalPageNum;

}
