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
* @className: 用户收款组
* @description: TODO
* @data: 2019/8/6 10:14
*/
@Data
@ToString
@Table(name = "user_payee_group")
public class UserPayeeGroup {
    //    收款群组id
    @Id
    @Column(name = "payee_group_id")
    private int payeeGroupId;
    //    收款群组名称
    @Column(name = "payee_group_name")
    private String payeeGroupName;
    //    群组所属用户id
    @Column(name = "user_id")
    private int userId;
    //    是否弃用，0不弃用，1弃用。
    @Column(name = "is_abandoned")
    private byte isAbandoned;
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
