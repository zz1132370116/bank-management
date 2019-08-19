package com.zl.dc.mapper;

import com.zl.dc.pojo.FundCollectionPlan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FundCollectionPlanDOMapper {

    List<FundCollectionPlan> selectFundCollectionBySchedule(Integer collectionMonth, Integer collectionDay);

    /**
     * @author: Redsheep
     * @Param userId 用户id
     * @return: List<FundCollectionPlan> 归集计划列表
     * @description: 根据用户id获取归集计划列表
     * @data: 2019/8/16 15:46
     */
    List<FundCollectionPlan> getFundCollectionPlanList(Integer userId);

    /**
     * @author: Redsheep
     * @Param userId 用户id
     * @Param bankCardId 银行卡id
     * @Param password 银行卡密码
     * @return: Integer 影响条数
     * @description: 更改归集计划的状态
     * @data: 2019/8/16 15:48
     */
    Integer updateFundCollectionPlanStatus(Integer planId, Byte status);

    /**
     * @author: Redsheep
     * @Param fundCollectionPlan
     * @return: Integer
     * @description: 添加归集计划
     * @data: 2019/8/16 16:15
     */
    Integer insert(FundCollectionPlan fundCollectionPlan);

}