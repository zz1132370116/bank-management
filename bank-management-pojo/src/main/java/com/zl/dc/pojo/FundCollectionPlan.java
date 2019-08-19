package com.zl.dc.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @version: V1.0
 * @author: lu
 * @className: 基金归集
 * @description:
 * @data: 2019/8/6 9:48
 */
@Data
@ToString
@Table(name = "fund_collection_plan")
public class FundCollectionPlan {
    /**
     * 归集计划id
     */
    @Id
    @Column(name = "plan_id")
    private Integer planId;
    /**
     * 资金归集计划名称
     */
    @Column(name = "plan_name")
    private String planName;
    /**
     * 资金归集计划的状态
     * 100 进行中
     * 101 已主动取消
     * 102 已手动终止
     * 103 已结束
     */
    @Column(name = "plan_status")
    private Byte planStatus;
    /**
     * 归集金额
     */
    private BigDecimal collectionAmount;
    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;
    /**
     * 转出卡
     */
    @Column(name = "bank_out_card_id")
    private Integer bankOutCardId;
    /**
     * 转入卡
     */
    @Column(name = "bank_in_card_id")
    private Integer bankInCardId;
    /**
     * 月份，1~12表示哪个月，0表示每月
     */
    @Column(name = "month")
    private Byte collectionMonth;
    /**
     * 日，1~31表示哪一天
     */
    @Column(name = "day")
    private Byte collectionDay;
    /**
     * 累计失败次数，超过3次取消计划
     */
    @Column(name = "fail_time")
    private Byte failTime;
    /**
     * 行创建时间
     */
    @Column(name = "gmt_create")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    /**
     * 行修改时间
     */
    @Column(name = "gmt_modified")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModified;
    /**
     * 归集状态转义
     */
    @Transient
    private String statusName;
    /**
     * 转出卡卡号
     */
    @Transient
    private String bankOutCard;
    /**
     * 转入卡卡号
     */
    @Transient
    private String bankInCard;
}
