package com.zl.dc.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @version: V1.0
 * @author: lu
 * @className: 建企业用户
 * @description:
 * @data: 2019/8/10 14:08
 */
@Data
@ToString
@Table(name = "bank_enterprise")
public class BankEnterprise {
    //    公司id
    @Id
    @Column(name = "enterprise_id")
    private int enterpriseId;
    //    公司名
    @Column(name = "enterprise_name")
    private String enterpriseName;
    //    公司注册码
    @Column(name = "enterprise_regno")
    private String enterpriseRegno;
    //    法人姓名
    @Column(name = "legalperson_name")
    private String legalpersonName;
    //    法人身份证
    @Column(name = "legalperson_id_card")
    private String legalpersonIdCard;
    //    法人手机号
    @Column(name = "legalperson_phone")
    private String legalpersonPhone;
    //    登录密码
    @Column(name = "enterprise_login_password")
    private String enterpriseLoginPassword;
    //    银行卡号，用来登录
    @Column(name = "enterprise_bank_card")
    private String enterpriseBankCard;
    //    企业状态
    @Column(name = "enterprise_status")
    private byte enterpriseStatus;
    //    行创建时间
    @Column(name = "gmt_create")
    private Timestamp gmtCreate;
    //    行修改时间
    @Column(name = "gmt_modified")
    private Timestamp gmtModified;

}
