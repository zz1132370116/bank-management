package com.zl.dc.service;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.zl.dc.mapper.BankCardMapper;
import com.zl.dc.mapper.OtherBankCardMapper;
import com.zl.dc.pojo.BankCard;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.OtherBankCard;
import com.zl.dc.util.MD5;
import com.zl.dc.vo.TransferValueVo;
import org.apache.commons.lang3.StringUtils;
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

    @Resource
    private OtherBankCardMapper otherBankCardMapper;

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
            if (StringUtils.isNotBlank(transferValueVo.getPassword())) {
//                对银行卡id进行非空校验
                if (transferValueVo.getOutBankCardID() != null) {
//            将传入密码加密处理
//                    transferValueVo.setPassword(MD5.GetMD5Code(transferValueVo.getPassword()));
//            拼接条件查询出该银行卡
                    criteria.andEqualTo("bankCardPassword", transferValueVo.getPassword());
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

        //          查询扣款卡，扣款
        BankCard outBankCard = bankCardMapper.selectByPrimaryKey(transferValueVo.getOutBankCardID());
        outBankCard.setBankCardBalance(outBankCard.getBankCardBalance().subtract(transferValueVo.getMuchMoney()));
        int transferOut = bankCardMapper.updateByPrimaryKeySelective(outBankCard);

        if (transferOut != 1) {
            //转账失败回滚操作
            return false;
        }

        //          查询收款卡，收款
        Example example = new Example(BankCard.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("bankCardNumber", transferValueVo.getInBankCard());
        BankCard inBankCard = bankCardMapper.selectOneByExample(example);
        inBankCard.setBankCardBalance(inBankCard.getBankCardBalance().add(transferValueVo.getMuchMoney()));
        int transfer = bankCardMapper.updateByPrimaryKeySelective(inBankCard);
        if (transfer != 1) {
            //转账失败回滚操作
            return false;
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
     * @author: lu
     * @Param String 银行卡号
     * @return: OtherBankCard
     * @description: 根据他行银行卡号查询银行
     * @data: 2019/8/13 14:18
     */
    public OtherBankCard getBankNameByBankNum(String BankNum) {

        Example example = new Example(OtherBankCard.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("bankCardNumber", BankNum);
        OtherBankCard otherBankCard = otherBankCardMapper.selectOneByExample(example);
        if (otherBankCard == null) {
            return null;
        }
        return otherBankCard;
    }

    /**
     * @author: lu
     * @Param String 银行卡号
     * @return: BankUser
     * @description: 根据银行卡号查询用户id
     * @data: 2019/8/13 20:32
     */
    public Integer selectBankUserByBankCardNum(String bankCardNum) {
        Example example = new Example(BankCard.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("bankCardNumber", bankCardNum);
        BankCard bankCard = bankCardMapper.selectOneByExample(example);
        return bankCard.getUserId();
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
