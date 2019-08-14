package com.zl.dc.mapper;

import com.zl.dc.pojo.TransferRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TransferRecordDOMapper {

    /**
     * @version: V1.0
     * @author: redsheep
     * @className: TransferRecordDOMapper
     * @description: 根据月份查转账记录
     * @data: 2019/8/13 13:56
     */
    List<TransferRecord> selectByUserIdAndMonthAndCard(@Param("userId") Integer userId,
                                                       @Param("startDay") String startDay,
                                                       @Param("endDay") String endDay,
                                                       @Param("index") Integer index,
                                                       @Param("limit") Integer limit,
                                                       @Param("bankCard") String bankCard);


    int deleteByPrimaryKey(Integer transferRecordId);

    int insert(TransferRecord record);

    int insertSelective(TransferRecord record);

    TransferRecord selectByPrimaryKey(Integer transferRecordId);

    int updateByPrimaryKeySelective(TransferRecord record);

    int updateByPrimaryKey(TransferRecord record);
}