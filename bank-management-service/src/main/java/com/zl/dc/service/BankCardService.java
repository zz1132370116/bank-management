package com.zl.dc.service;

import com.zl.dc.mapper.BankCardMapper;
import com.zl.dc.pojo.BankCard;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.util.MD5;
import com.zl.dc.vo.TransferValueVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.PrintStream;
import java.util.List;

/**
 * @version: V1.0
 * @author: lu
 * @className: 银行卡业务层
 * @description:
 * @data: 2019/8/12 11:00
 */
@Service
@Transactional
public class BankCardService {

    @Resource
    private BankCardMapper bankCardMapper;


    /**
     * @author: lu
     * @param: TransferValueVo
     * @return: BankCard
     * @description: 根据VO校验银行卡
     * @data: 2019/8/12 11:06
     */

    public BankCard verifyBankCardForVo(TransferValueVo transferValueVo) {
        Example example = new Example(BankCard.class);
        Example.Criteria criteria = example.createCriteria();
//          对VO进非空校验
        if (transferValueVo != null) {
//            对密码进行非空校验
            if (transferValueVo.getPassword() != null && !"".equals(transferValueVo.getPassword())) {
                criteria.andEqualTo("bankCardPassword", transferValueVo.getPassword());
//                对银行卡id进行非空校验
                if (transferValueVo.getOutBankCardID() != null && !"".equals(transferValueVo.getOutBankCardID())) {
//            将传入密码加密处理
//                    transferValueVo.setPassword(MD5.GetMD5Code(transferValueVo.getPassword()));
//            拼接条件查询出该银行卡
                    criteria.andEqualTo("bankCardId", transferValueVo.getOutBankCardID());
                    criteria.andEqualTo("bankCardStatus", "100");
                    return bankCardMapper.selectOneByExample(example);

                }
            }
        }
        return null;
    }

    /**
     * @author: lu
     * @param: * TransferValueVo
     * @return: * boolean
     * @description: 银行卡转账业务
     * @data: 2019/8/12 16:19
     */
    public boolean bankCardTransferBusines(TransferValueVo transferValueVo) {
        try {
            //          查询扣款卡，扣款

            BankCard outBankCard = bankCardMapper.selectByPrimaryKey(transferValueVo.getOutBankCardID());
            outBankCard.setBankCardBalance(outBankCard.getBankCardBalance().subtract(transferValueVo.getMuchMoney()));
            int transferOut = bankCardMapper.updateByPrimaryKeySelective(outBankCard);


            //          查询收款卡，收款
            Example example = new Example(BankCard.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("bankCardNumber", transferValueVo.getInBankCard());
            BankCard inBankCard = bankCardMapper.selectOneByExample(example);
            inBankCard.setBankCardBalance(inBankCard.getBankCardBalance().add(transferValueVo.getMuchMoney()));
            int transfer = bankCardMapper.updateByPrimaryKeySelective(inBankCard);
        } catch (Exception e) {
            throw e;
        }
        return true;
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
        criteria.andEqualTo("userId", bankUser.getUserId());
        List<BankCard> bankCards = bankCardMapper.selectByExample(example);
        for (BankCard bankCard : bankCards) {
            bankCard.setBank("五仁银行");
        }
        return bankCards;
    }

    /**
     * @author: zhanglei
     * @param: [bankCardId]
     * @return:com.zl.dc.pojo.BankCard
     * @description: 根据银行卡ID查询银行卡信息
     * @data: 2019/8/14 11:35
     */
    public BankCard getBankCardBybankCardId(String bankCardId) {
        BankCard bankCard = bankCardMapper.selectByPrimaryKey(Integer.parseInt(bankCardId));

        return bankCard;
    }
}
