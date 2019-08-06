package com.zl.dc.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * @version: V1.0
 * @author: lu
 * @className: 基金收集
 * @description:
 * @data: 2019/8/6 9:48
 */
@Data
@ToString
@Table(name = "fund_collection_plan")
public class FundCollectionPlan {
    //    主键
    @Id
    @Column(name = "plan_id")
    private int planId;
    //    用户id
    @Column(name = "user_id")
    private int userId;
    //    转出卡
    @Column(name = "bank_out_card")
    private String bankOutCard;
    //    转入卡
    @Column(name = "bank_in_card")
    private String bankInCard;
    //    月份，1~12表示哪个月，0表示每月
    @Column(name = "month")
    private byte month;
    //    日，1~31表示哪一天
    @Column(name = "day")
    private byte day;
    //    累计失败次数，超过3次取消计划
    @Column(name = "fail_time")
    private byte failTime;
    //    行创建时间
    @Column(name = "gmt_create")
    private Date gmtCreate;
    //    行修改时间
    @Column(name = "gmt_modified")
    private Date gmtModified;


}
