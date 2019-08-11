package com.zl.dc.service;

import com.zl.dc.api.exchangeRateApi;
import com.zl.dc.mapper.CrossBorderTransferRecordMapper;
import com.zl.dc.pojo.CrossBorderTransferRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
  * @version: V1.0
  * @author: zhanglei
  * @className: CrossBorderTransferRecordService
  * @description: 跨境转账操作层
  * @data: 2019/8/10 16:44
  */
 @Service
 @Transactional
public class CrossBorderTransferRecordService {
     @Resource
    private CrossBorderTransferRecordMapper crossBorderTransferRecordMapper;

    public CrossBorderTransferRecord getExchangeRate(CrossBorderTransferRecord crossBorderTransferRecord) {
        String amount = null;
        String from ="CNY";
        String to =null;
        //和0，Zero比较
        int f = crossBorderTransferRecord.getTransferRecordAmountFrom().compareTo(BigDecimal.ZERO);
        //和0，Zero比较
        int t = crossBorderTransferRecord.getTransferRecordAmountTo().compareTo(BigDecimal.ZERO);
        //判断交易额不为空
        if ((f == 0 || f == -1) && crossBorderTransferRecord.getCurrencyType().equals("")) {
            amount = crossBorderTransferRecord.getTransferRecordAmountFrom().toString();
            to = crossBorderTransferRecord.getCurrencyType();
            //判断外币不为空
        }else if ((t == 0 || t == -1) && crossBorderTransferRecord.getCurrencyType().equals("")){
            amount =crossBorderTransferRecord.getTransferRecordAmountTo().toString();
            to = crossBorderTransferRecord.getCurrencyType();
        }
        String s = exchangeRateApi.exChangeRate(amount, from, to);
        crossBorderTransferRecord.setRate(s);
        return crossBorderTransferRecord;
    }

}
