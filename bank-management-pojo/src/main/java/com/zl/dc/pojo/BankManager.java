package com.zl.dc.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
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


}
