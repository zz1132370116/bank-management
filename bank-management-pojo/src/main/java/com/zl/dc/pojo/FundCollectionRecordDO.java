package com.zl.dc.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
public class FundCollectionRecordDO {

    /**
     * 主键
     */
    private Integer planRecordId;

    /**
     * 归集计划的id
     */
    private Integer planId;

    /**
     * 转账记录的uuid
     */
    private String recordUuid;

    private Date gmtCreate;

    private Date gmtModified;
}