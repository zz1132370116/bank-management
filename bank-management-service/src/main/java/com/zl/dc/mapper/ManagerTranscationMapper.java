package com.zl.dc.mapper;

import com.zl.dc.pojo.ManagerTranscation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @version: V1.0
 * @author: zhanglei
 * @className: ManagerTranscationMapper
 * @description: 事务持久层
 * @data: 2019/8/6 13:59
 */
@org.apache.ibatis.annotations.Mapper
public interface ManagerTranscationMapper extends Mapper<ManagerTranscation> {
    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.ManagerTranscation>
     * @description: 查询用户申请中的提卡信息
     * @data: 2019/8/9 15:03
     */
    @Select("SELECT transcation_id,manager_id,bank_card,user_id,transcation_status,transcation_type,transcation_msg,gmt_create,gmt_modified\n" +
            "\tFROM manager_transcation WHERE transcation_status=0 and transcation_id> (#{pageNum}-1)*10 LIMIT 20;")
    @Results(id="transferRecordMap", value={
            @Result(column = "transcation_id",property = "transcationId"),
            @Result(column = "manager_id",property = "managerId"),
            @Result(column = "bank_card",property = "bankCard"),
            @Result(column = "user_id",property = "userId"),
            @Result(column = "transcation_status",property = "transcationStatus"),
            @Result(column = "transcation_type",property = "transcationType"),
            @Result(column = "transcation_msg",property = "transcationMsg"),
            @Result(column = "gmt_create",property = "gmtCreate"),
            @Result(column = "gmt_modified",property = "gmtModified"),

    })
    List<ManagerTranscation> getManagerTranscations(@Param("pageNum") Integer pageNum);
     /**
      * @author: zhanglei
      * @param:
      * @return:
      * @description: 查询事务数量
      * @data: 2019/8/21 14:36
      */
    @Select("select count(transcation_id) from manager_transcation")
    Integer selectcount();
}
