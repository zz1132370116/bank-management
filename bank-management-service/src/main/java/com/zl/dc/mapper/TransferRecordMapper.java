package com.zl.dc.mapper;

import com.zl.dc.pojo.TransferRecord;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

/**
* @version: V1.0
* @author: redsheep
* @className: TransferRecordMapper
* @description: 转账记录表mapper
* @data: 2019/8/12 10:52
*/
@org.apache.ibatis.annotations.Mapper
public interface TransferRecordMapper extends Mapper<TransferRecord> {

    
    List<TransferRecord> getRecordsByBankCardAndDate(String BankCard);

}
