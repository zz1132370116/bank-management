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
/*    @Select("SELECT\n" +
            "transfer_record_uuid,\n" +
            "transfer_record_amount,\n" +
            "transfer_status,\n" +
            "bank_out_card,\n" +
            "in_card_user_name,\n" +
            "bank_name,\n" +
            "bank_in_card\n" +
            "FROM(\n" +
            "SELECT \n" +
            "transfer_record_uuid,\n" +
            "transfer_record_amount,\n" +
            "transfer_status,\n" +
            "bank_out_card,\n" +
            "in_card_user_name,\n" +
            "bank_in_identification,\n" +
            "bank_in_card\n" +
            "FROM transfer_record WHERE user_id = 1 limit 10,20) t,\n" +
            "subordinate_bank s\n" +
            "WHERE s.bank_identification = t.bank_in_identification")
    List<TransferRecord> getRecordsByParams(String idCard, Date startDate, Date endDate);*/
}
