package com.zl.dc.mapper;

import com.zl.dc.pojo.FundCollectionRecordDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FundCollectionRecordDOMapper {

    /**
    * @author: Redsheep
    * @Param record
    * @return: int
    * @description: 插入归集记录
    * @data: 2019/8/19 17:16
    */
    Integer insert(FundCollectionRecordDO record);
}