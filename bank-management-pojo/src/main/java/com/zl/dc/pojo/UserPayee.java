package com.zl.dc.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
/**
 * @version: V1.0
 * @author: lu
 * @className: 用户收款人
 * @description:
 * @data: 2019/8/6 10:11
 */
@Data
@ToString
@Table(name = "user_payee")
public class UserPayee {
    //    主键
    @Id
    @Column(name = "payee_id")
    private int payeeId;
    //    收款人姓名
    @Column(name = "payee_name")
    private String payeeName;
    //    银行卡
    @Column(name = "payee_bank_card")
    private String payeeBankCard;
    //    银行标识
    @Column(name = "payee_bank_identification")
    private String payeeBankIdentification;
    //    收款群组群组
    @Column(name = "payee_group_id")
    private int payeeGroupId;
    //    添加时间
    @Column(name = "gmt_create")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    //    修改时间
    @Column(name = "gmt_modified")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModified;

    //自定义标签，银行的名字
    @Transient
    private String bankName;
}
