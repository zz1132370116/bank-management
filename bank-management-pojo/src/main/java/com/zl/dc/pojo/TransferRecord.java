package com.zl.dc.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
/**
* @version: V1.0
* @author: lu
* @className: 转账记录
* @description:
* @data: 2019/8/6 10:02
*/
@Data
@ToString
@Table(name = "transfer_record")
public class TransferRecord {
    //    主键
    @Id
    @Column(name = "transfer_record_id")
    private int transferRecordId;
    //    转账记录编号
    @Column(name = "transfer_record_uuid")
    private String transferRecordUuid;
    //    交易额
    @Column(name = "transfer_record_amount")
    private BigDecimal transferRecordAmount;
    //    订单时间
    @Column(name = "transfer_record_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date transferRecordTime;
    //    交易状态
    @Column(name = "transfer_status")
    private byte transferStatus;
    //备注
    @Column(name = "transfer_note")
    private String transferNote;
    //收账人姓名
    @Column(name = "in_card_user_name")
    private String inCardUserName;
    //    用户id
    @Column(name = "user_id")
    private int userId;
    //    转出卡
    @Column(name = "bank_out_card")
    private String bankOutCard;
 /**
  * @author: zhanglei
  * @param:
  * @return:
  * @description: 转入卡
  * @data: 2019/8/6 19:05
  */
    @Column(name = "bank_in_card")
    private String bankInCard;
 /**
  * @author: zhanglei
  * @param:
  * @return:
  * @description: 创建时间
  * @data: 2019/8/6 19:05
  */
    @Column(name = "gmt_create")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
     /**
      * @author: zhanglei
      * @param:
      * @return:
      * @description: 修改时间
      * @data: 2019/8/6 19:05
      */
    @Column(name = "gmt_modified")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModified;

    @Transient
    private BankUser bankUser;
     /**
      * @author: zhanglei
      * @param:
      * @return:
      * @description: 开始时间(用于条件查询)
      * @data: 2019/8/6 19:04
      */
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

     /**
      * @author: zhanglei
      * @param:
      * @return:
      * @description: 结束时间(用于条件查询)
      * @data: 2019/8/6 19:04
      */
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    /**
     * @author: zhanglei
     * @param:
     * @return:
     * @description: 转入银行标识
     * @data: 2019/8/7 9:29
     */
    @Column(name = "bank_in_identification")
    private String bankInIdentification;
     /**
      * @author: zhanglei
      * @param:
      * @return:
      * @description: 身份证号
      * @data: 2019/8/7 9:28
      */
    @Transient
    private String idCard;
     /**
      * @author: zhanglei
      * @param:
      * @return:
      * @description: 转出银行
      * @data: 2019/8/7 9:29
      */
    @Transient
    private String bankOutCardName;
     /**
      * @author: zhanglei
      * @param:
      * @return:
      * @description: 转入银行
      * @data: 2019/8/7 9:29
      */
     @Transient
     private String bankInCardName;
     @Transient
    private String userName;

}
