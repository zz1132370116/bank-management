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
 * @className: 管理员操作表
 * @description:
 * @data: 2019/8/6 9:51
 */
@Data
@ToString
@Table(name = "manager_transcation")
public class ManagerTranscation {
    //    主键
    @Id
    @Column(name = "transcation_id")
    private int transcationId;
    //    管理员ID
    @Column(name = "manager_id")
    private Integer managerId;
    //    银行卡号
    @Column(name = "bank_card")
    private String bankCard;
    //    用户id
    @Column(name = "user_id")
    private Integer userId;
    //    事务状态
    @Column(name = "transcation_status")
    private byte transcationStatus;
    //    事务类型
    @Column(name = "transcation_type")
    private byte transcationType;
    //    事务信息
    @Column(name = "transcation_msg")
    private String transcationMsg;
    //    创建时间
    @Column(name = "gmt_create")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    //    修改时间
    @Column(name = "gmt_modified")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModified;
    @Transient
    private BankUser bankUser;

}
