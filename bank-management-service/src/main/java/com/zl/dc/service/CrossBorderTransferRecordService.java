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
import java.util.List;
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
        //保留两位小数
        BigDecimal RMB = new BigDecimal(price);
        //保留两位小数
        BigDecimal bigDecima = new BigDecimal(s);
        crossBorderTransferRecord.setTransferRecordAmountFrom(RMB.setScale(2, BigDecimal.ROUND_DOWN));
        crossBorderTransferRecord.setTransferRecordAmountTo(bigDecima.setScale(2, BigDecimal.ROUND_DOWN));
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
        //保留两位小数
        BigDecimal RMB = new BigDecimal(s);
        //保留两位小数
        BigDecimal bigDecima = new BigDecimal(price);
        crossBorderTransferRecord.setTransferRecordAmountFrom(RMB.setScale(2, BigDecimal.ROUND_DOWN));
        crossBorderTransferRecord.setTransferRecordAmountTo(bigDecima.setScale(2, BigDecimal.ROUND_DOWN));
        return crossBorderTransferRecord;
    }

    /**
     * @author: zhanglei
     * @param: [crossBorderTransferRecord]
     * @return:void
     * @description: 跨境转账
     * @data: 2019/8/13 10:15
     */
    public String CrossBorderTransfer(CrossBorderTransferRecord crossBorderTransferRecord) {
        //条件
        Example example = new Example(BankCard.class);
        Example.Criteria criteria = example.createCriteria();
        Example example1 = new Example(CrossBorderTransferRecord.class);
        Example.Criteria criteria1 = example1.createCriteria();
        //转账总金额
        BigDecimal bigDecimal =null;

        //非空判断
        if (StringUtils.isNotBlank(crossBorderTransferRecord.getBankOutCard())
                && StringUtils.isNotBlank(crossBorderTransferRecord.getBankInCard())
                && StringUtils.isNotBlank(crossBorderTransferRecord.getCurrencyType())) {
            //查询当前卡的转账记录
            criteria1.andEqualTo("bankOutCard",crossBorderTransferRecord.getBankOutCard());
            List<CrossBorderTransferRecord> crossBorderTransferRecords = crossBorderTransferRecordMapper.selectByExample(example1);
            for (CrossBorderTransferRecord borderTransferRecord : crossBorderTransferRecords) {
                bigDecimal = bigDecimal.add(borderTransferRecord.getTransferRecordAmountFrom());
            }
            criteria.andEqualTo("bankCardNumber", crossBorderTransferRecord.getBankOutCard());
            //条件查询银行卡
            BankCard bankCard = bankCardMapper.selectOneByExample(example);
            //判断银行卡限额
            if (bankCard.getBankCardType().equals("普通卡")){
                if (bigDecimal.compareTo(new BigDecimal("500000"))==1){
                    return "已超出银行卡限额,请前往提升卡类型";
                }
            }else if (bankCard.getBankCardType().equals("钻石卡")){
                if (bigDecimal.compareTo(new BigDecimal("1000000"))==1){
                    return "已超出银行卡限额,请前往提升卡类型";
                }
            }else if (bankCard.getBankCardType().equals("黑卡")){
                if (bigDecimal.compareTo(new BigDecimal("10000000"))==1){
                    return "已超出银行卡限额,请前往提升卡类型";
                }
            }else{
                //类型转换
                int from = crossBorderTransferRecord.getTransferRecordAmountFrom().compareTo(BigDecimal.ZERO);
                int to = crossBorderTransferRecord.getTransferRecordAmountTo().compareTo(BigDecimal.ZERO);
                //非0判断
                if (from != 0 && from != -1 && to != 0 && to != -1) {
                    //转账
                    //修改银行卡金额

                    //判断银行卡不为空并且银行卡可用
                    if (bankCard != null && bankCard.getBankCardStatus() == 0) {
                        int i = bankCard.getBankCardBalance().compareTo(BigDecimal.ZERO);
                        //银行卡余额不为空 并且 银行卡余额大于转账金额
                        if (i != 0 && bankCard.getBankCardBalance().compareTo(crossBorderTransferRecord.getTransferRecordAmountFrom()) == 1) {
                            bankCard.setBankCardBalance(bankCard.getBankCardBalance().subtract(crossBorderTransferRecord.getTransferRecordAmountFrom()));
                            bankCardMapper.updateByPrimaryKeySelective(bankCard);
                            //创建转账机记录
                            //生成流水号
                            String uuid = UUID.randomUUID().toString();

                            crossBorderTransferRecord.setTransferRecordUuid( uuid.replaceAll("-", ""));
                            //生成创建时间
                            crossBorderTransferRecord.setGmtCreate(new Date());
                            //生成交易类型
                            crossBorderTransferRecord.setTransferType(Byte.parseByte("0"));
                            //生成交易时间
                            crossBorderTransferRecord.setTransferRecordTime(new Date());
                            //生成修改时间
                            crossBorderTransferRecord.setGmtModified(new Date());
                            //生成转账记录
                            crossBorderTransferRecordMapper.insertSelective(crossBorderTransferRecord);
                            return "转账成功";
                        } else {
                            return "余额不足";
                        }
                    }
                } else {
                    return "转账失败";
                }
            }

        } else {
            return "数据为空";
        }
        return null;
    }
}
