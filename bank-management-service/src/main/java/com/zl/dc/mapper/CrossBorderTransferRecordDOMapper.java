package com.zl.dc.mapper;

import com.zl.dc.pojo.CrossBorderTransferRecord;
import com.zl.dc.pojo.TransferRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CrossBorderTransferRecordDOMapper {

    /**
     * @version: V1.0
     * @author: redsheep
     * @className: TransferRecordDOMapper
     * @description: 根据月份查转账记录
     * @data: 2019/8/13 13:56
     */
    List<CrossBorderTransferRecord> selectByUserIdAndMonthAndCard(Integer userId, String startDay, String endDay,
                                                                  Integer index, Integer limit, String bankCard);

}