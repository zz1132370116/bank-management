package com.zl.dc.mapper;

import com.zl.dc.pojo.TransferRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TransferRecordDOMapper {

    /**
    * @author: Redsheep
    * @Param planId 归集计划id
    * @return: List<TransferRecord>
    * @description: 选择归集计划id查出归集计划记录
    * @data: 2019/8/19 9:17
    */
    List<TransferRecord> selectFundCollectionRecord(Integer planId);

    /**
     * @version: V1.0
     * @author: redsheep
     * @className: TransferRecordDOMapper
     * @description: 根据月份查转账记录
     * @data: 2019/8/13 13:56
     */
    List<TransferRecord> selectByUserIdAndMonthAndCard(Integer userId,String startDay,String endDay,
                                                       Integer index,Integer limit,String bankCard);

    int deleteByPrimaryKey(Integer transferRecordId);

    int insert(TransferRecord record);

    int insertSelective(TransferRecord record);

    TransferRecord selectByPrimaryKey(Integer transferRecordId);

    int updateByPrimaryKeySelective(TransferRecord record);

    int updateByPrimaryKey(TransferRecord record);
}