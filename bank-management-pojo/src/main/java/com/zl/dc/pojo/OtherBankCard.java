package com.zl.dc.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @version: V1.0
 * @author: lu
 * @className: 其他银行卡
 * @description:
 * @data: 2019/8/6 10:15
 */
@Data
@ToString
@Table(name = "other_bank_card")
public class OtherBankCard {
    //    主键
    @Id
    @Column(name = "bank_card_id")
    private int bankCardId;
    //    银行卡号
    @Column(name = "bank_card_number")
    private String bankCardNumber;
    //    用户id
    @Column(name = "user_id")
    private int userId;
    //    银行标识码
    @Column(name = "subordinate_banks_identification")
    private String subordinateBanksIdentification;
    //    行创建时间
    @Column(name = "gmt_create")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    //    行修改时间
    @Column(name = "gmt_modified")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModified;

}

