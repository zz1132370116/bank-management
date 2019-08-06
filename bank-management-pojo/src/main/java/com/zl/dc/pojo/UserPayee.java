package com.zl.dc.pojo;

import lombok.Data;
import lombok.ToString;

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
    private Date gmtCreate;
    //    修改时间
    @Column(name = "gmt_modified")
    private Date gmtModified;

}
