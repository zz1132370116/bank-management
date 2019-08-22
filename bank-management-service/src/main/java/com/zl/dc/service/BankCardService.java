package com.zl.dc.service;

import com.zl.dc.mapper.*;
import com.zl.dc.pojo.BankCard;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.ManagerTranscation;
import com.zl.dc.pojo.OtherBankCard;
import com.zl.dc.util.MD5;
import com.zl.dc.util.StarUtil;
import com.zl.dc.vo.BankUserVo;
import com.zl.dc.vo.TransferValueVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
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
    @Resource
    private ManagerTranscationMapper managerTranscationMapper;
    @Resource
    private OtherBankCardDOMapper otherBankCardDOMapper;

    /**
     * @author: lu
     * @param: TransferValueVo
     * @return: boolean
     * @description: 银行卡 密码校验
     * @data: 2019/8/15 11:06
     */
    public boolean BankCardPasswordCheck(BankCard bankCard, String password) {
//            将传入密码加密处理
        String BankCardPasswod = MD5.GetMD5Code(password);
        return BankCardPasswod.equals(bankCard.getBankCardPassword());
    }

    /**
     * @author: lu
     * @Param Integer bankCardId
     * @return: BankCard
     * @description: 根据银行卡id查询银行卡
     * @data: 2019/8/15 16:15
     */
    public BankCard selectBankCardByid(Integer bankCardId) {
        return bankCardMapper.selectByPrimaryKey(bankCardId);
    }

    /**
     * @author: lu
     * @Param String bankCardNumber
     * @return: BankCard
     * @description: 根据银行卡号查询银行卡
     * @data: 2019/8/15 16:15
     */
    public BankCard selectBankCardByNum(String bankCardNumber) {
        Example example = new Example(BankCard.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("bankCardNumber", bankCardNumber);
        return bankCardMapper.selectOneByExample(example);
    }


    /**
     * @author: lu
     * @param: Integer outBankCardId
     * @param: String inBankCardId
     * @param: BigDecimal muchMoney
     * @return: * boolean
     * @description: 银行卡转账业务
     * @data: 2019/8/12 16:19
     */
    public boolean bankCardTransferBusines(Integer outBankCardId, String inBankCardId, BigDecimal muchMoney) {

        BankCard outBankCard = selectBankCardByid(outBankCardId);
        if (outBankCard == null) {
            return false;
        }
        if ("9999".equals(inBankCardId.substring(0, 4))) {
            BankCard inBankCard = selectBankCardByNum(inBankCardId);
            if (inBankCard == null) {
                return false;
            }
            //          查询收款卡，收款
            inBankCard.setBankCardBalance(inBankCard.getBankCardBalance().add(muchMoney));
            bankCardMapper.updateByPrimaryKeySelective(inBankCard);

        } else {
            OtherBankCard inBankCard = getBankNameByBankNum(inBankCardId);
            if (inBankCard == null) {
                return false;
            }
        }

        //          查询扣款卡，扣款
        outBankCard.setBankCardBalance(outBankCard.getBankCardBalance().subtract(muchMoney));
        bankCardMapper.updateByPrimaryKeySelective(outBankCard);
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


    /**
     * @param userId
     * @return java.util.List<com.zl.dc.pojo.BankCard>
     * @author pds
     * @description 根据用户Id获取该用户其他银行的银行卡
     * @date 2019/8/14 12:45
     */
    public List<OtherBankCard> getOtherBankCardByUserId(Integer userId) {
        Example example = new Example(OtherBankCard.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        List<OtherBankCard> otherBankCards = otherBankCardMapper.selectByExample(example);

        return otherBankCards;
    }

    /**
     * @author: zhanglei
     * @param: [bankCardId]
     * @return:com.zl.dc.pojo.BankCard
     * @description: 根据银行卡ID查询银行卡信息
     * @data: 2019/8/14 11:35
     */
    public BankCard getBankCardByBankCardId(String bankCardId) {
        BankCard bankCard = bankCardMapper.selectByPrimaryKey(Integer.parseInt(bankCardId));
        return bankCard;
    }

    /**
     * @author: zhanglei
     * @param: [bankCard]
     * @return:java.lang.String
     * @description: 申请升级卡
     * @data: 2019/8/14 16:00
     */
    public String UpgradeCard(BankCard bankCard) {
        BankCard bankCard1 = bankCardMapper.selectByPrimaryKey(bankCard);
        ManagerTranscation managerTranscation = new ManagerTranscation();
        if (StringUtils.isNotBlank(bankCard1.getBankCardNumber())) {
            managerTranscation.setBankCard(bankCard1.getBankCardNumber());
        } else {
            return "缺少银行卡信息";
        }
        if (bankCard.getUserId() != null) {
            managerTranscation.setUserId(bankCard1.getUserId());
        } else {
            return "缺少用户信息";
        }
        //申请中状态为0
        managerTranscation.setTranscationStatus(Byte.parseByte("0"));
        //申请提升类型 0
        managerTranscation.setTranscationType(Byte.parseByte("0"));
        if (StringUtils.isNotBlank(bankCard.getTranscationMsg())) {
            managerTranscation.setTranscationMsg(bankCard.getTranscationMsg());
        }
        managerTranscation.setGmtCreate(new Date());
        int i = managerTranscationMapper.insertSelective(managerTranscation);
        if (i != 0) {
            return "申请成功";
        } else {
            return "申请失败";
        }
    }

    /**
     * @author: Redsheep
     * @Param otherBankCard
     * @return: boolean
     * @description: 绑定他行卡
     * @data: 2019/8/14 16:16
     */
    public boolean addOtherBankCard(OtherBankCard otherBankCard) {
        Date now = new Date();
        otherBankCard.setGmtModified(now);
        otherBankCard.setGmtCreate(now);
        if (otherBankCardDOMapper.insertSelective(otherBankCard) == 1) {
            return true;
        }
        return false;
    }

    /**
     * @author: Redsheep
     * @Param otherBankCard
     * @return: boolean
     * @description: 查看该银行卡是否已存在数据库中
     * @data: 2019/8/14 16:24
     */
    public boolean selectByUserIdAndCardNumber(OtherBankCard otherBankCard) {
        return otherBankCardDOMapper.selectByUserIdAndCardNumber(otherBankCard) != null;
    }

    /**
     * @author: Redsheep
     * @Param otherBankCardId
     * @return: boolean
     * @description: 解绑他行卡
     * @data: 2019/8/15 19:14
     */
    public boolean deleteByOtherBankCardId(Integer otherBankCardId) {
        return otherBankCardDOMapper.deleteByOtherBankCardId(otherBankCardId) != null;
    }

    /**
     * @author: Redsheep
     * @Param bankCardId
     * @return: boolean
     * @description: 挂失本行卡
     * @data: 2019/8/15 19:25
     */
    public boolean reportBankCardLoss(Integer bankCardId) {
        return bankCardDOMapper.updateBankCardStatus(bankCardId, Byte.parseByte("101")) != null;
    }

    /**
     * @author: Redsheep
     * @Param bankCardId
     * @Param password
     * @return: boolean
     * @description: 验证银行卡密码是否正确
     * @data: 2019/8/15 19:27
     */
    public boolean verifyBankCardPassword(Integer bankCardId, String password, Integer userId) {
        return bankCardDOMapper.selectByBankCardIdAndPassword(bankCardId, password, userId) != null;
    }

    /**
     * @author: Redsheep
     * @Param otherBankCardId
     * @Param userId
     * @return: boolean
     * @description: 验证他行卡密码是否正确
     * @data: 2019/8/15 20:01
     */
    public boolean verifyOtherBankCardPassword(Integer otherBankCardId, Integer userId) {
        return otherBankCardDOMapper.selectByOtherBankCardIdAndPassword(otherBankCardId, userId) != null;
    }

    /**
     * @author: Redsheep
     * @Param bankCardId
     * @return: java.lang.String
     * @description: 根据银行卡id获得银行卡
     * @data: 2019/8/19 16:32
     */
    public String selectBankCardNumberById(Integer bankCardId) {
        return bankCardDOMapper.selectBankCardNumberById(bankCardId);
    }


    /**
     * @author pds
     * @param enterpriseBankCardNumber
     * @param userBankCardNumber
     * @param bankInIdentification
     * @param money
     * @return java.lang.Boolean
     * @description 企业批量转账
     * @date 2019/8/20 13:43
     */
    public Boolean enterpriseTransfer(String enterpriseBankCardNumber, String userBankCardNumber, String bankInIdentification, BigDecimal money){
        BankCard enterpriseBankCard = selectBankCardByNum(enterpriseBankCardNumber);
        if (enterpriseBankCard == null){
            return false;
        }
        //本行卡
        if ("999999".equals(userBankCardNumber.substring(0,6))){
            BankCard bankCard = selectBankCardByNum(userBankCardNumber);
            if (bankCard == null){
                return false;
            }
            BigDecimal balance = bankCard.getBankCardBalance().add(money);
            bankCard.setBankCardBalance(balance);
            Integer user = bankCardMapper.updateByPrimaryKeySelective(bankCard);
            if (user == 0){
                return false;
            }
        }
        //非本行卡
        else {
            switch (bankInIdentification){
                case "PSBC":{
                    //调用中国邮政储蓄银行的转账接口
                    break;
                }
                case "ABC":{
                    //调用中国农业银行的转账接口
                    break;
                }
                default:{

                }
            }
        }
        BigDecimal enterpriseBalance = enterpriseBankCard.getBankCardBalance().subtract(money);
        enterpriseBankCard.setBankCardBalance(enterpriseBalance);
        Integer enterprise = bankCardMapper.updateByPrimaryKeySelective(enterpriseBankCard);
        if (enterprise == 0){
            return false;
        }
        return true;
    }
}
