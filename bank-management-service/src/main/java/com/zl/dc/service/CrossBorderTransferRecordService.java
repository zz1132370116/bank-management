package com.zl.dc.service;

import com.zl.dc.api.exchangeRateApi;
import com.zl.dc.mapper.BankCardMapper;
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

        String amount = "100";
        String from = "CNY";
        String to = null;
        if (!crossBorderTransferRecord.getCurrencyType().equals("") && crossBorderTransferRecord.getCurrencyType() != null) {
            to = crossBorderTransferRecord.getCurrencyType();
            String s = exchangeRateApi.exChangeRate(amount, from, to);
            crossBorderTransferRecord.setRate(s);
        }
        return crossBorderTransferRecord;
    }
    /**
     * @author: zhanglei
     * @param: [crossBorderTransferRecord]
     * @return:com.zl.dc.pojo.CrossBorderTransferRecord
     * @description: 换算金额
     * @data: 2019/8/12 9:34
     */
    public CrossBorderTransferRecord getExchange(CrossBorderTransferRecord crossBorderTransferRecord) {

        String from = "CNY";
        //和0，Zero比较
        int f = crossBorderTransferRecord.getTransferRecordAmountFrom().compareTo(BigDecimal.ZERO);
        //和0，Zero比较
        int t = crossBorderTransferRecord.getTransferRecordAmountTo().compareTo(BigDecimal.ZERO);
        //判断交易额是否存在
        if (f == 0 || f == -1){
            //判断到账金额是否为空
            if (t == 0 || t == -1){
                //根据外币向人民币转换
                String s = exchangeRateApi.exChangeRatePrice(String.valueOf(t), crossBorderTransferRecord.getCurrencyType(), from );
               crossBorderTransferRecord.setTransferRecordAmountFrom(new BigDecimal(s));
                return crossBorderTransferRecord;
            }else{
                return null;
            }
        }else{
            //存在
            //根据外币向人民币转换
            String s = exchangeRateApi.exChangeRatePrice(String.valueOf(t),from , crossBorderTransferRecord.getCurrencyType() );
            crossBorderTransferRecord.setTransferRecordAmountTo(new BigDecimal(s));
            return crossBorderTransferRecord;
        }
    }

}
