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

    /**
     * @author: zhanglei
     * @param: [crossBorderTransferRecord]
     * @return:com.zl.dc.pojo.CrossBorderTransferRecord
     * @description: 查询汇率
     * @data: 2019/8/12 9:24
     */
    public CrossBorderTransferRecord getExchangeRate(CrossBorderTransferRecord crossBorderTransferRecord) {

        String amount = "10";
        String from = "CNY";
        String to = null;
        if (crossBorderTransferRecord.getCurrencyType().equals("") && crossBorderTransferRecord.getCurrencyType() != null) {
            String s = exchangeRateApi.exChangeRate(amount, from, to);
            crossBorderTransferRecord.setRate(s);
        }
        return crossBorderTransferRecord;
    }

}
