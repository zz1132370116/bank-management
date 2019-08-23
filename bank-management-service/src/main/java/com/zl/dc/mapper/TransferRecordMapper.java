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
    @Select("\tSELECT transfer_record_id,transfer_record_uuid,transfer_record_amount,transfer_record_time,transfer_note,transfer_status,transfer_type,user_id,bank_out_card,in_card_user_name,bank_in_card,gmt_create,gmt_modified ,bank_in_identification FROM transfer_record WHERE  transfer_record_uuid > (#{pageNum}-1)*10 LIMIT 10;")
    @Results(id="transferRecordMap", value={
            @Result(column = "transfer_record_id",property = "transferRecordId"),
            @Result(column = "transfer_record_uuid",property = "transferRecordUuid"),
            @Result(column = "transfer_record_amount",property = "transferRecordAmount"),
            @Result(column = "transfer_record_time",property = "transferRecordTime"),
            @Result(column = "transfer_note",property = "transferNote"),
            @Result(column = "transfer_status",property = "transferStatus"),
            @Result(column = "transfer_type",property = "transferType"),
            @Result(column = "user_id",property = "userId"),
            @Result(column = "bank_out_card",property = "bankOutCard"),
            @Result(column = "in_card_user_name",property = "inCardUserName"),
            @Result(column = "bank_in_identification",property = "bankInIdentification"),
            @Result(column = "bank_in_card",property = "bankInCard"),
            @Result(column = "gmt_create",property = "gmtCreate"),
            @Result(column = "gmt_modified",property = "gmtModified")
    })
    List<TransferRecord> GetRecords(@Param("pageNum") Integer pageNum);
     /**
      * @author: zhanglei
      * @param:
      * @return:
      * @description: 查询记录数量
      * @data: 2019/8/21 14:35
      */
    @Select(" EXPLAIN SELECT * FROM transfer_record")
    Integer selectcount();

    @Select("select transfer_record_id,transfer_record_uuid,transfer_record_amount,transfer_record_time,transfer_note,transfer_status,transfer_type,user_id,bank_out_card,in_card_user_name,bank_in_card,gmt_create,gmt_modified ,bank_in_identification from transfer_record WHERE  transfer_record_uuid =#{transfer_record_uuid} and transfer_status = 100")
    @Results( value={
            @Result(column = "transfer_record_id",property = "transferRecordId"),
            @Result(column = "transfer_record_uuid",property = "transferRecordUuid"),
            @Result(column = "transfer_record_amount",property = "transferRecordAmount"),
            @Result(column = "transfer_record_time",property = "transferRecordTime"),
            @Result(column = "transfer_note",property = "transferNote"),
            @Result(column = "transfer_status",property = "transferStatus"),
            @Result(column = "transfer_type",property = "transferType"),
            @Result(column = "user_id",property = "userId"),
            @Result(column = "bank_out_card",property = "bankOutCard"),
            @Result(column = "in_card_user_name",property = "inCardUserName"),
            @Result(column = "bank_in_identification",property = "bankInIdentification"),
            @Result(column = "bank_in_card",property = "bankInCard"),
            @Result(column = "gmt_create",property = "gmtCreate"),
            @Result(column = "gmt_modified",property = "gmtModified")
    })
    TransferRecord selectTransferRecordByTransferRecordUuid(@Param("transfer_record_uuid") String transactionRecordUUID);
}
