package com.zl.dc.service;

import com.zl.dc.mapper.BankCardMapper;
import com.zl.dc.pojo.BankCard;
import com.zl.dc.util.MD5;
import com.zl.dc.vo.TransferValueVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @version: V1.0
 * @author: lu
 * @className: 银行卡业务层
 * @description:
 * @data: 2019/8/12 11:00
 */
@Service
@Transactional(rollbackFor = Exception.class)
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
//          对VO进非空校验
        if (transferValueVo != null) {
//            对密码进行非空校验
            if (transferValueVo.getPassword() != null && "".equals(transferValueVo.getPassword())) {
//                对银行卡id进行非空校验
                if (transferValueVo.getOutBankCardID() != null && "".equals(transferValueVo.getOutBankCardID())) {
//            将传入密码加密处理
                    transferValueVo.setPassword(MD5.GetMD5Code(transferValueVo.getPassword()));
//            拼接条件查询出该银行卡
                    Example example = new Example(BankCard.class);
                    Example.Criteria criteria = example.createCriteria();
                    criteria.andEqualTo("bankCardId", transferValueVo.getOutBankCardID());
                    criteria.andEqualTo("bankCardPassword", transferValueVo.getPassword());
                    criteria.andEqualTo("bankCardType", 100);
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
        int transferOut = bankCardMapper.updateByExample(outBankCard, BankCard.class);

        //          查询收款卡，收款
        BankCard inBankCard = bankCardMapper.selectByPrimaryKey(transferValueVo.getInBankCard());
        inBankCard.setBankCardBalance(inBankCard.getBankCardBalance().add(transferValueVo.getMuchMoney()));
        int transfer = bankCardMapper.updateByExample(inBankCard, BankCard.class);

        if ((transferOut + transfer) == 2) {
            return true;
        }
        return false;
    }

}
