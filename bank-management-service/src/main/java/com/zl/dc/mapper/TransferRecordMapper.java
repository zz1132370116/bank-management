package com.zl.dc.mapper;

import com.zl.dc.pojo.TransferRecord;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @Auther: 舌頭會游泳
 * @Date: 2019/8/3 16:39
 * @Description:
 */
@org.apache.ibatis.annotations.Mapper
public interface TransferRecordMapper extends Mapper<TransferRecord> {
    @Select("SELECT tr.* FROM transfer_record tr,bank_user us WHERE #{idCard}=us.id_card AND tr.gmt_create >= #{startDate} AND tr.gmt_create <= #{endDate}")
    @Results({
            @Result(column = "transfer_record_id",property = "transferRecordId"),
            @Result(column = "transfer_record_uuid",property = "transferRecordUuid"),
            @Result(column = "transfer_record_amount",property = "transferRecordAmount"),
            @Result(column = "transfer_record_time",property = "transferRecordTime"),
            @Result(column = "transaction_status",property = "transactionStatus"),
            @Result(column = "user_id",property = "userId"),
            @Result(column = "bank_out_card",property = "bankOutCard"),
            @Result(column = "bank_in_card",property = "bankInCard"),
            @Result(column = "gmt_create",property = "gmtCreate"),
            @Result(column = "gmt_modified",property = "gmtModified")
    })
    List<TransferRecord> getRecordsByParams( TransferRecord transferRecord);
}
