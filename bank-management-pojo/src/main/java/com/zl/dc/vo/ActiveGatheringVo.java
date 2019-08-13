package com.zl.dc.vo;

import lombok.Data;

import java.math.BigDecimal;
/**
 * @version V1.0
 * @author nwm
 * @className ActiveGatheringVo
 * @description 收款人与付款人页面展示使用
 * @date 2019/8/12 20:14
 */
@Data
public class ActiveGatheringVo {
    //主动收款人id
    private Integer inUserId;
    //主动收款人姓名
    private String inUserName;
    //收款人银行卡归属行
    private String inBank;
    //收款人卡号
    private  String inBankCard;

    //付款人id
    private Integer outUserId;
    //付款人姓名
    private String outUserName;
    //付款人银行卡归属行
    private String outBank;
    //付款人卡号
    private  String outBankCard;

    //订单id
    private Integer activeId;
    //订单金额
    private BigDecimal muchMoney;
    //订单备注
    private String transferRemarks;
    //订单类型
    private Integer activeType;
    //订单状态
    private Integer activestate;


    @Override
    public String toString() {
        return "ActiveGatheringVo{" +
                "inUserId=" + inUserId +
                ", inUserName='" + inUserName + '\'' +
                ", inBank='" + inBank + '\'' +
                ", inBankCard='" + inBankCard + '\'' +
                ", outUserId=" + outUserId +
                ", outUserName='" + outUserName + '\'' +
                ", outBank='" + outBank + '\'' +
                ", outBankCard='" + outBankCard + '\'' +
                ", activeId=" + activeId +
                ", muchMoney=" + muchMoney +
                ", transferRemarks='" + transferRemarks + '\'' +
                ", activeType=" + activeType +
                ", activestate=" + activestate +
                '}';
    }
}
