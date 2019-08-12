package com.zl.dc.vo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @version: V1.0
 * @author: lu
 * @className: 单次转账页面Vo
 * @description:
 * @data: 2019/8/12 11:13
 */
@Data
@ToString
public class TransferValueVo {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 付款银行卡id
     */
    private Integer outBankCardID;
    /**
     * 付款银行卡
     */
    private String outBankCard;
    /**
     * 收款的银行
     */
    private String inBank;
    /**
     * 收款的银行卡号
     */
    private String inBankCard;
    /**
     * 收款人姓名
     */
    private String inBankName;
    /**
     * 转账金额
     */
    private BigDecimal muchMoney;
    /**
     * 转账时间,0,代表即时转账，2代表2小时转账
     */
    private String transferTime;
    /**
     * 转账备注
     */

    private String transferRemarks;

    /**
     * 密码
     */
    private String password;

}
