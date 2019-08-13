package com.zl.dc.service;

import com.zl.dc.api.exchangeRateApi;
import com.zl.dc.mapper.BankCardMapper;
import com.zl.dc.mapper.CrossBorderTransferRecordMapper;
import com.zl.dc.pojo.BankCard;
import com.zl.dc.pojo.CrossBorderTransferRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


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
    @Resource
    private BankCardMapper bankCardMapper;

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
     * @param: [price, type]
     * @return:com.zl.dc.pojo.CrossBorderTransferRecord
     * @description: 根据人民币查询外币
     * @data: 2019/8/13 9:41
     */
    public CrossBorderTransferRecord getExchangeRatePrice(String price, String type) {
        //类型转换
        //根据人民币转为用户选择币种的金额
        String s = exchangeRateApi.exChangeRatePrice(price, "CNY", type);
        CrossBorderTransferRecord crossBorderTransferRecord = new CrossBorderTransferRecord();
        crossBorderTransferRecord.setCurrencyType(type);
        crossBorderTransferRecord.setTransferRecordAmountFrom(new BigDecimal(price));
        crossBorderTransferRecord.setTransferRecordAmountTo(new BigDecimal(s));
        return crossBorderTransferRecord;
    }

    /**
     * @author: zhanglei
     * @param: [price, type]
     * @return:com.zl.dc.pojo.CrossBorderTransferRecord
     * @description: 根据外币查询人民币
     * @data: 2019/8/13 9:42
     */
    public CrossBorderTransferRecord getExchangeRateCNY(String price, String type) {
        //类型转换
        //根据人民币转为用户选择币种的金额
        String s = exchangeRateApi.exChangeRatePrice(price, type, "CNY");
        CrossBorderTransferRecord crossBorderTransferRecord = new CrossBorderTransferRecord();
        crossBorderTransferRecord.setCurrencyType(type);
        crossBorderTransferRecord.setTransferRecordAmountFrom(new BigDecimal(s));
        crossBorderTransferRecord.setTransferRecordAmountTo(new BigDecimal(price));
        return crossBorderTransferRecord;
    }

    /**
     * @author: zhanglei
     * @param: [crossBorderTransferRecord]
     * @return:void
     * @description: 跨境转账
     * @data: 2019/8/13 10:15
     */
    public void CrossBorderTransfer(CrossBorderTransferRecord crossBorderTransferRecord) {
        Example example = new Example(BankCard.class);
        Example.Criteria criteria = example.createCriteria();
        //非空判断
        if (StringUtils.isNotBlank(crossBorderTransferRecord.getBankOutCard()) && StringUtils.isNotBlank(crossBorderTransferRecord.getBankInCard()) && StringUtils.isNotBlank(crossBorderTransferRecord.getCurrencyType())) {
            int from = crossBorderTransferRecord.getTransferRecordAmountFrom().compareTo(BigDecimal.ZERO);
            int to = crossBorderTransferRecord.getTransferRecordAmountTo().compareTo(BigDecimal.ZERO);
            //非0判断
            if (from != 0 && from != -1 && to != 0 && to != -1) {
                //转账
                //修改银行卡金额
                criteria.andEqualTo("bankCardNumber", crossBorderTransferRecord.getBankOutCard());
                BankCard bankCard = bankCardMapper.selectOneByExample(example);
                bankCard.setBankCardBalance(bankCard.getBankCardBalance().subtract(crossBorderTransferRecord.getTransferRecordAmountFrom()));
                bankCardMapper.updateByPrimaryKeySelective(bankCard);
                //创建转账机记录
                //生成流水号
                crossBorderTransferRecord.setTransferRecordUuid(UUID.randomUUID().toString());
                //生成创建时间
                crossBorderTransferRecord.setGmtCreate(new Date());
                //
                crossBorderTransferRecordMapper.insertSelective(crossBorderTransferRecord);
            }

        }
    }
}
