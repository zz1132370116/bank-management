package com.zl.dc.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @version V1.0
 * @author pds
 * @className EnterpriseEmployee
 * @description 企业转账员工实体类
 * @date 2019/8/16 11:51
 */
@Data
public class EnterpriseEmployee {
    private String userName;
    private String userBankCardName;
    private String userBankCardNumber;
    private BigDecimal money;
    private String bankInIdentification;
    private String status;
}
