package com.zl.dc.service;

import com.zl.dc.mapper.BankCardDOMapper;
import com.zl.dc.mapper.BankCardMapper;
import com.zl.dc.mapper.OtherBankCardMapper;
import com.zl.dc.pojo.BankCard;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.OtherBankCard;
import com.zl.dc.util.StarUtil;
import com.zl.dc.vo.TransferValueVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.ListIterator;

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
    private BankCardDOMapper bankCardDOMapper;
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
     * @author: Redsheep
     * @Param userId
     * @return: java.util.List<com.zl.dc.pojo.BankCard>
     * @description: 根据用户id选出所有状态为100的本行卡信息，包括id、卡号、类型、转账限额
     * @data: 2019/8/14 9:06
     */
    public List<BankCard> getBankCardByUserId(Integer userId) {
        List<BankCard> bankCards = bankCardDOMapper.selectBankCardListByUserId(userId);
        ListIterator<BankCard> bankCardListIterator = bankCards.listIterator();
        BankCard bankCard;
        String bankCardNumber;
        // 银行卡号加*
        while (bankCardListIterator.hasNext()) {
            bankCard = bankCardListIterator.next();
            bankCardNumber = bankCard.getBankCardNumber();
            bankCard.setBankCardNumber(StarUtil.StringAddStar(bankCardNumber, 4, 4));
            bankCard.setBank("五仁银行");
        }
        return bankCards;
    }

    /**
     * @author: Redsheep
     * @Param status
     * @return: java.lang.String
     * @description: 银行卡状态显义
     * @data: 2019/8/14 9:14
     */
    private String changeBankCardStatus(String status) {
        switch (status) {
            case "100":
                return "正常";
            case "101":
                return "挂失";
            case "102":
                return "冻结";
            case "103":
                return "注销";
            default:
                return "未知";
        }
    }

}
