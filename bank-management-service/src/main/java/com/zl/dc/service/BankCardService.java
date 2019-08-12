package com.zl.dc.service;

import com.zl.dc.mapper.BankCardMapper;
import com.zl.dc.pojo.BankCard;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.util.MD5;
import com.zl.dc.vo.transferValueVo;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version: V1.0
 * @author: lu
 * @className: 银行卡业务层
 * @description:
 * @data: 2019/8/12 11:00
 */
@Service
public class BankCardService {

    @Resource
    private BankCardMapper bankCardMapper;


    /**
     * @author: lu
     * @param: transferValueVo
     * @return: BankCard
     * @description: 根据VO校验银行卡
     * @data: 2019/8/12 11:06
     */
    public BankCard verifyBankCardForVo(transferValueVo transferValueVo) {
        // TODO 对银行卡号进行验证操作，本行直接调用数据库库，他行则调用接口
//          对VO进非空校验
        if (transferValueVo != null) {
//            对密码进行非空校验
            if (transferValueVo.getPassword() != null && "".equals(transferValueVo.getPassword())) {
//                对银行卡id进行非空校验
                if (transferValueVo.getOutBankCardID() != null && "".equals(transferValueVo.getOutBankCardID())) {
//            将传入密码加密处理
                    transferValueVo.setPassword(MD5.GetMD5Code(transferValueVo.getPassword()));
//            查询出该银行卡
                    return bankCardMapper.verifyBankCardForVo(transferValueVo);
                }
            }
        }
        return null;
    }
    /**
     * @author: zhanglei
     * @param: [bankUser]
     * @return:java.util.List<com.zl.dc.pojo.CrossBorderTransferRecord>
     * @description: 根据用户查询用户所持有的本行卡
     * @data: 2019/8/12 15:26
     */
    public List<BankCard> getBankCardByUser(BankUser bankUser) {
        Example example = new Example(BankCard.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",bankUser.getUserId());
        List<BankCard> bankCards = bankCardMapper.selectByExample(example);
        for (BankCard bankCard : bankCards) {
            bankCard.setBank("五仁银行");
        }
        return bankCards;
    }

}
