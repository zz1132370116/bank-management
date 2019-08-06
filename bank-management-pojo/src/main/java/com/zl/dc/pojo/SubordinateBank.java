package com.zl.dc.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * @version: V1.0
 * @author: lu
 * @className: 所属银行
 * @description:
 * @data: 2019/8/6 9:58
 */
@Data
@ToString
@Table(name = "subordinate_bank")
public class SubordinateBank {
    //    所属银行id
    @Id
    @Column(name = "bank_id")
    private int bankId;
    //    所属银行标识
    @Column(name = "bank_identification")
    private String bankIdentification;
    //    银行名字
    @Column(name = "bank_name")
    private String bankName;

}
