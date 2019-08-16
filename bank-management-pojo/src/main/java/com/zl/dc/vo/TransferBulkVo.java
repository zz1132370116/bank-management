package com.zl.dc.vo;

import com.zl.dc.pojo.BankCard;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
* @version: V1.0
* @author: lu
* @className: TransferBulkVo
* @description: 批量转账页面VO
* @data: 2019/8/16 11:21
*/
@Data
@ToString
public class TransferBulkVo {
    /**
     * 本人银行卡
     */
    private BankCard bankCard;
    /**
     * 群组收款人
     */
    private List<NewPayeeVo> payee;

}
