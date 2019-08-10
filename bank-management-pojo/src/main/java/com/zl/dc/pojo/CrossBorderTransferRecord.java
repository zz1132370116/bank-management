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
 * @className: 跨境转账表
 * @description:
 * @data: 2019/8/10 14:02
 */
@Data
@ToString
@Table(name = "cross_border_transfer_record")
public class CrossBorderTransferRecord {
    //    主键
    @Id
    @Column(name = "transfer_record_id")
    private int transferRecordId;
    //    流水号
    @Column(name = "transfer_record_uuid")
    private String transferRecordUuid;
    //    交易额，人民币
    @Column(name = "transfer_record_amount_from")
    private BigDecimal transferRecordAmountFrom;
    //    交易额，外币
    @Column(name = "transfer_record_amount_to")
    private BigDecimal transferRecordAmountTo;
    //    币种
    @Column(name = "currency_type")
    private String currencyType;
    //    交易时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "transfer_record_time")
    private Date transferRecordTime;
    //    转账备注
    @Column(name = "transfer_note")
    private String transferNote;
    //    交易状态，无符号
    @Column(name = "transfer_status")
    private byte transferStatus;
    //    交易类型
    @Column(name = "transfer_type")
    private byte transferType;
    //    用户id，无符号
    @Column(name = "user_id")
    private int userId;
    //    转出卡号
    @Column(name = "bank_out_card")
    private String bankOutCard;
    //    收款人姓名
    @Column(name = "in_card_user_name")
    private String inCardUserName;
    //    转入卡号
    @Column(name = "bank_in_card")
    private String bankInCard;
    //    行创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "gmt_create")
    private Date gmtCreate;
    //    修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "gmt_modified")
    private Date gmtModified;

}
