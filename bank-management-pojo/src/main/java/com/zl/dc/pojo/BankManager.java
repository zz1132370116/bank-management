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
 * @className: 管理员
 * @description:
 * @data: 2019/8/6 9:37
 */
@Data
@ToString
@Table(name = "bank_manager")
public class BankManager {
    //    主键
    @Id
    @Column(name = "manager_id")
    private int managerId;
    //    管理员用户名
    @Column(name = "manager_name")
    private String managerName;
    //    管理员密码
    @Column(name = "manager_password")
    private String managerPassword;
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",  timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginDate;


}
