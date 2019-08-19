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
    /**
     * @author: zhanglei
     * @param: [idCard, startDate, endDate]
     * @return:java.util.List<com.zl.dc.pojo.TransferRecord>
     * @description: 条件查询记录
     * @data: 2019/8/6 19:18
     */
    @Select("SELECT transfer_record_id,transfer_record_uuid,transfer_record_amount,transfer_record_time,transfer_note,transfer_status,\n" +
            "\ttransfer_type,user_id,bank_out_card,in_card_user_name,bank_in_card,gmt_create,gmt_modified\n" +
            "\tFROM transfer_record WHERE bank_out_card =#{idCard} AND gmt_create >=#{startDate} AND gmt_create<#{endDate} AND transfer_record_uuid > (#{pageNum}-1)*10 LIMIT 10;")
    List<TransferRecord> getRecordsByParams(@Param("idCard") String idCard, @Param("startDate") Date startDate, @Param("endDate") Date endDate,@Param("pageNum") Integer pageNum);
    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.TransferRecord>
     * @description: 查询所有记录(分页)
     * @data: 2019/8/6 19:18
     */
    @Select("SELECT transfer_record_id,transfer_record_uuid,transfer_record_amount,transfer_record_time,transfer_note,transfer_status,\n" +
            "\ttransfer_type,user_id,bank_out_card,in_card_user_name,bank_in_card,gmt_create,gmt_modified\n" +
            "\tFROM transfer_record WHERE  transfer_record_uuid > (#{pageNum}-1)*10 LIMIT 10;")
    List<TransferRecord> GetRecords(@Param("pageNum") Integer pageNum);
}
