package com.zl.dc.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;
@Data
public class Gathering {
    /**收款记录主键*/
    @Id
    @Column(name = "gathering_id")
    private Integer gatheringId;
    /**转账记录uuid*/
    @Column(name = "transaction_record_UUID")
    private String transactionRecordUUID;
    /**付款人id*/
    @Column(name = "payment_user_id")
    private Integer paymentUserId;
    /**收款状态:
     * 100收款中
     * 101收款结束*/
    @Column(name = "gathering_status")
    private Integer gatheringStatus;
    /***/
    @Column(name = "creation_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;
    /***/
    @Column(name = "modification_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modificationTime;
}
