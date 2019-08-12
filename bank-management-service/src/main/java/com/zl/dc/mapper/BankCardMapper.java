package com.zl.dc.mapper;

import com.zl.dc.pojo.BankCard;
import com.zl.dc.vo.transferValueVo;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
@org.apache.ibatis.annotations.Mapper
public interface BankCardMapper extends Mapper<BankCard> {


    /**
    * @author: lu
    * @param: * transferValueVo
    * @return: * BankCard
    * @description: 根据VO校验银行卡
    * @data: 2019/8/12 11:42
    */
    @Select("SELECT * FROM bank_card WHERE bank_card_id=#{outBankCardID} AND bank_card_password=#{password} AND bank_card_type=100")
    public BankCard verifyBankCardForVo(transferValueVo transferValueVo);
}
