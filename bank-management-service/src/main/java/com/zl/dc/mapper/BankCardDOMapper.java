package com.zl.dc.mapper;

import com.zl.dc.pojo.BankCard;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BankCardDOMapper {


    /**
     * @author: Redsheep
     * @Param bankCardId 银行卡id
     * @param userId 用户id
     * @param password 支付密码
     * @return: Integer 银行卡id
     * @description:
     * @data: 2019/8/15 19:31
     */
    Integer selectByBankCardIdAndPassword(Integer bankCardId, String password, Integer userId);

    /**
     * @author: Redsheep
     * @Param bankCardId 本行卡id
     * @Param status 状态
     * @return: Integer 影响条数
     * @description:
     * @data: 2019/8/15 17:55
     */
    Integer updateBankCardStatus(Integer bankCardId, Byte status);

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