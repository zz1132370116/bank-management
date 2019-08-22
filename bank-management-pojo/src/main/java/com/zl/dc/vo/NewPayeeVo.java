package com.zl.dc.vo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @version: V1.0
 * @author: lu
 * @className: NewPayeeVo
 * @description: 收款群主收款人VO
 * @data: 2019/8/14 15:20
 */
@Data
@ToString
public class NewPayeeVo {
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 银行卡号
     */
    private String payeeBankCard;
    /**
     * 银行标识
     */
    private String payeeBankIdentification;
    /**
     * 所属收款群组
     */
    private Integer payeeGroupId;
    /**
     * 转账金额
     */
    private BigDecimal muchMoney;
    /**
     * 所属银行名
     */
    private String bankCardName;

}
