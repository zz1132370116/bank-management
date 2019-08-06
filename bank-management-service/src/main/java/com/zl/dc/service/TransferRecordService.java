package com.zl.dc.service;

import com.zl.dc.mapper.TransferRecordMapper;
import com.zl.dc.mapper.UserMapper;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.TransferRecord;
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

    /**
     * @author: zhanglei
     * @param: [idCard, startDate, endDate]
     * @return:java.util.List<com.zl.dc.pojo.TransferRecord>
     * @description: 条件查询记录
     * @data: 2019/8/6 19:18
     */
    public List<TransferRecord> getRecordsByParams(String idCard, Date startDate, Date endDate) {
        //查询所有
        TransferRecord transferRecord = new TransferRecord();

        if (!idCard.equals("")){
            transferRecord.setIdCard(idCard);
        }else if (startDate !=null){
            transferRecord.setStartDate(startDate);
    }else if (endDate!=null){
            transferRecord.setEndDate(endDate);
        }
        List<TransferRecord> transferRecords = transferRecordMapper.getRecordsByParams(transferRecord);
        return transferRecords;
    }


}
