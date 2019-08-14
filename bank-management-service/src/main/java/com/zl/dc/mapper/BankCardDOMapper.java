package com.zl.dc.mapper;

import com.zl.dc.pojo.BankCard;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BankCardDOMapper {

    /**
    * @author: Redsheep
    * @Param bankCardId
    * @return: String
    * @description: 根据银行卡id查银行卡号
    * @data: 2019/8/13 20:37
    */
    String selectBankCardNumberById(Integer bankCardId);

    /**
    * @author: Redsheep
    * @Param userId
    * @return: List<BankCard>
    * @description: 根据用户id查询用户所有的本行卡
    * @data: 2019/8/13 20:39
    */
    List<BankCard> selectBankCardListByUserId(Integer userId);

    int deleteByPrimaryKey(Integer bankCardId);

    int insert(BankCard record);

    int insertSelective(BankCard record);

    BankCard selectByPrimaryKey(Integer bankCardId);

    int updateByPrimaryKeySelective(BankCard record);

    int updateByPrimaryKey(BankCard record);
}