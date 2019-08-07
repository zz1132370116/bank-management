package com.zl.dc.service;

import com.alibaba.fastjson.JSONObject;
import com.zl.dc.mapper.SubordinateBankMapper;
import com.zl.dc.mapper.TransferRecordMapper;
import com.zl.dc.mapper.UserMapper;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.SubordinateBank;
import com.zl.dc.pojo.TransferRecord;
import com.zl.dc.util.AccessBank;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @version: V1.0
 * @author: zhanglei
 * @className: TransferRecordService
 * @description: 转账记录操作层
 * @data: 2019/8/6 19:15
 */
@Service
@Transactional
public class TransferRecordService {
    @Resource
    private TransferRecordMapper transferRecordMapper;
    @Resource
    private UserMapper bankUserMapper;
    @Resource
    private SubordinateBankMapper subordinateBankMapper;

    /**
     * @author: zhanglei
     * @param: [idCard, startDate, endDate]
     * @return:java.util.List<com.zl.dc.pojo.TransferRecord>
     * @description: 条件查询记录
     * @data: 2019/8/6 19:18
     */
    public List<TransferRecord> getRecordsByParams(String idCard, Date startDate, Date endDate) {
        //创建空对象
        TransferRecord transferRecord = new TransferRecord();
        //创建银行卡条件
        Example example = new Example(SubordinateBank.class);
        Example.Criteria criteria = example.createCriteria();
        //创建记录条件
        Example example1 = new Example(TransferRecord.class);
        Example.Criteria criteria1 = example1.createCriteria();
        //非空判断
        if (!idCard.equals("")) {
            //拼接条件
            criteria1.andEqualTo("idCard",idCard);
        } else if (startDate != null) {
            //拼接条件
            criteria1.andGreaterThan("gmt_create",startDate);
        } else if (endDate != null) {
            //拼接条件
            criteria1.andLessThan("gmt_create",endDate);
        }
        //条件查询
        List<TransferRecord> transferRecords = transferRecordMapper.selectByExample(example1);
        //遍历
        for (TransferRecord record : transferRecords) {
            //通过用户id查询当前用户信息
            BankUser bankUser = bankUserMapper.selectByPrimaryKey(record.getUserId());
            //赋值
            record.setBankUser(bankUser);
            //获取转出卡所属银行
            String bankOut = AccessBank.getCardDetail(record.getBankOutCard());
            JSONObject jsonOut = JSONObject.parseObject(bankOut);
            criteria.andEqualTo("bankIdentification", jsonOut.get("bank"));
            SubordinateBank subordinateBank = subordinateBankMapper.selectOneByExample(example);
            record.setBankOutCardName(subordinateBank.getBankName());
            //获取转入卡所属银行
            String bankIn = AccessBank.getCardDetail(record.getBankInCard());
            JSONObject jsonIn = JSONObject.parseObject(bankIn);
            criteria.andEqualTo("bankIdentification", jsonIn.get("bank"));
            SubordinateBank selectOneByExample = subordinateBankMapper.selectOneByExample(example);
            record.setBankInCardName(selectOneByExample.getBankName());
        }
        return transferRecords;
    }


}
