package com.zl.dc.pojo;

import lombok.Data;
import lombok.ToString;

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
    private int userId;
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
    private byte userStatus;
    //    用户身份证号
    @Column(name = "id_card")
    private String idCard;
    //    创建时间
    @Column(name = "gmt_create")
    private Date gmtCreate;
    //    修改时间
    @Column(name = "gmt_modified")
    private Date gmtModified;


}
