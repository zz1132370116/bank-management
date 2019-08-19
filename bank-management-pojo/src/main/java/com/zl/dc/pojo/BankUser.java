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
 * @className: 用户
 * @description:
 * @data: 2019/8/6 9:38
 */
@Data
@ToString
@Table(name = "bank_user")
public class BankUser {
    //    主键自增
    @Id
    @Column(name = "user_id")
    private Integer userId;
    //    用户姓名
    @Column(name = "user_name")
    private String userName;
    //    用户电话
    @Column(name = "user_phone")
    private String userPhone;
    //    用户密码
    @Column(name = "user_password")
    private String userPassword;
    //    用户状态
    @Column(name = "user_status")
    private Byte userStatus;
    //    手机号收款的银行卡，默认为空
    @Column(name = "default_bank_card")
    private String defaultBankCard;
    //    用户身份证号
    @Column(name = "id_card")
    private String idCard;
    //    创建时间
    @Column(name = "gmt_create")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    //    修改时间
    @Column(name = "gmt_modified")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModified;
    //页码
    @Transient
    private Integer pageNum;
    //总页数
    @Transient
    private Integer totalPageNum;

}
