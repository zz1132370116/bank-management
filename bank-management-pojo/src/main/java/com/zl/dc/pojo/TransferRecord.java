package com.zl.dc.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * @version: V1.0
 * @author: lu
 * @className: 转账记录表
 * @description:
 * @data: 2019/8/10 13:46
 */
@Data
@ToString
@Table(name = "transfer_record")
public class TransferRecord {
    //    主键
    @Id
    @Column(name = "transfer_record_id")
    private int transferRecordId;
    //    转账记录流水号
    @Column(name = "transfer_record_uuid")
    private String transferRecordUuid;
    //    交易额
    @Column(name = "transfer_record_amount")
    private BigDecimal transferRecordAmount;
    //    订单时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "transfer_record_time")
    private Date transferRecordTime;
    //    转账备注
    @Column(name = "transfer_note")
    private String transferNote;
    //    交易状态
    @Column(name = "transfer_status")
    private byte transferStatus;
    //    转账类型
    @Column(name = "transfer_type")
    private byte transferType;
    //    用户id
    @Column(name = "user_id")
    private int userId;
    //    转出卡
    @Column(name = "bank_out_card")
    private String bankOutCard;
    //    收款人姓名
    @Column(name = "in_card_user_name")
    private String inCardUserName;
    //    转入卡的银行标识码
    @Column(name = "bank_in_identification")
    private String bankInIdentification;
    //    转入卡
    @Column(name = "bank_in_card")
    private String bankInCard;
    //    创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "gmt_create")
    private Date gmtCreate;
    //    修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "gmt_modified")
    private Date gmtModified;

    //用户省份证号，自定义属性
    @Transient
    private String idCard;
    //自定义属性，作为条件查询依据
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    //自定义属性，作为条件查询依据
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
}
