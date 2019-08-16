package com.zl.dc.service;


import com.zl.dc.api.AccessBank;
import com.zl.dc.mapper.*;
import com.zl.dc.pojo.*;

import com.zl.dc.util.StarUtil;
import com.zl.dc.vo.ActiveGatheringVo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @version: V1.0
 * @author: nwm
 * @className: ActiveGatheringService
 * @description: 主动收款相关业务层
 * @data: 2019/8/13
 */
@Service
@Transactional
public class ActiveGatheringService {
    //转账记录Mapper
    @Resource
    private TransferRecordMapper transferRecordMapper;
    //管理员事务表Mapper
    @Resource
    private ManagerTranscationMapper managerTranscationMapper;
    //银行卡Mapper
    @Resource
    private BankCardMapper bankCardMapper;
    @Resource
     private UserMapper userMapper;
    @Resource
    private  SubordinateBankMapper subordinateBankMapper;
    @Resource
    BankCardService bankCardService;

    /**
     * @author: nwm
     * @param: * getActiveGatheringVoList
     * @return: * List<ActiveGatheringVo>
     * @description: 查询所有主动收款记录
     * @data: 2019/8/14 19:00
     */
    public List<ActiveGatheringVo> getActiveGatheringVoList(Integer userId) {
        //获取当前登录用户iduserId
        TransferRecord transferRecord=new TransferRecord();
        Example example = new Example(TransferRecord.class);
        Example.Criteria criteria = example.createCriteria();
        transferRecord.setUserId(userId);
        //拼接查询条件
        criteria.andEqualTo("transferType","102");
        criteria.andEqualTo("userId",transferRecord.getUserId());
        //获取相关记录
        List<TransferRecord> transferRecordList = transferRecordMapper.selectByExample(example);
        if(transferRecordList==null){
            return null;
        }
        //拼接到ActiveGatheringVo
        List<ActiveGatheringVo> activeGatheringVoList=new ArrayList<ActiveGatheringVo>();
        for (TransferRecord transfer :transferRecordList){
            ActiveGatheringVo activeGatheringVo = new ActiveGatheringVo();
            //订单id
            activeGatheringVo.setActiveId(transfer.getTransferRecordId());
            //付款人姓名
            activeGatheringVo.setOutUserName(transfer.getInCardUserName());
            //收款卡
            activeGatheringVo.setInBankCard(StarUtil.StringAddStar(transfer.getBankOutCard(),4,4));
            //收款额
            activeGatheringVo.setMuchMoney(transfer.getTransferRecordAmount());
            //收款备注
            activeGatheringVo.setTransferRemarks(transfer.getTransferNote());
            //收款状态
            activeGatheringVo.setActivestate(new Integer(transfer.getTransferStatus()));
            activeGatheringVoList.add(activeGatheringVo);
        }
         return activeGatheringVoList;
    }

    /**
     * @author: nwm
     * @param: * updateGatheringStatus
     * @return: * boolean
     * @description: 用户修改主动收款状态为取消时执行的方法,及付款用户拒绝付款
     * @data: 2019/8/14 19:00
     */
    public boolean updateGatheringStatus(String activeId){
        TransferRecord transferRecord=new TransferRecord();
        //获取前台要修改的交易记录订单id
        transferRecord.setTransferRecordId(new Integer(activeId));
        //修改交易订单id的状态为取消
        transferRecord.setTransferStatus(Byte.parseByte("102"));
        transferRecord.setGmtModified(new Date());
        //通过transferRecordMapper操作记录表
        int status = transferRecordMapper.updateByPrimaryKeySelective(transferRecord);
        if (status > 0) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * @author: nwm
     * @param: * addTransactionTecord
     * @return: * boolean
     * @description: 用户添加主动收款时执行的方法
     * @data: 2019/8/14 19:00
     */
    public boolean addTransactionTecord(ActiveGatheringVo agvo){
        //根据发起主动收款用户填写的数据添加交易订单
        //agvo 收款订单基本信息
        TransferRecord transferRecord=new TransferRecord();
        //添加自动生成的交易流水号
        transferRecord.setTransferRecordUuid(UUID.randomUUID().toString().replaceAll("-", ""));
        //添加交易额
        transferRecord.setTransferRecordAmount(agvo.getMuchMoney());
        //添加交易时间
        transferRecord.setTransferRecordTime(new Date());
        //添加交易备注
        transferRecord.setTransferNote(agvo.getTransferRemarks());
        //添加交易类型为主动收款
        transferRecord.setTransferType(Byte.parseByte("102"));
        //添加交易状态为交易中
        transferRecord.setTransferStatus(Byte.parseByte("100"));
        //添加收款用户id
        transferRecord.setUserId(agvo.getInUserId());
        /*根据收款卡id 查询收款卡
        * userBankId */
        BankCard card = bankCardMapper.selectByPrimaryKey(agvo.getInBankId());
        //添加收款卡
        transferRecord.setBankOutCard(card.getBankCardNumber());
        /*根据付款人手机号查询付款人姓名
            userPhone 付款人电话*/
        Example example = new Example(BankUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userPhone",agvo.getOutUserPhone());
        BankUser bankUser = userMapper.selectOneByExample(example);
        if (bankUser==null){
            return false;
        }
        //添加付款人姓名
        transferRecord.setInCardUserName(bankUser.getUserName());
        //付款卡标识暂时为空
        transferRecord.setBankInIdentification("0");
        //付款卡暂时为空
        transferRecord.setBankInCard("0");
        transferRecord.setGmtCreate(new Date());
        transferRecord.setGmtModified(new Date());
        //添加转账记录
        int status=transferRecordMapper.insertSelective(transferRecord);
        if (status > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @author: nwm
     * @param: * getActiveGatheringVo
     * @return: * HashMap<String,Object>
     * @description: 查询相关待付款记录
     * @data: 2019/8/14 19:00
     */
    public List<ActiveGatheringVo> getActiveGatheringVo(String userName){
        //获取当前登录用户姓名 userName
        TransferRecord transferRecord=new TransferRecord();
        Example example = new Example(TransferRecord.class);
        Example.Criteria criteria = example.createCriteria();
        //查询付款人是当前登录用户的
        transferRecord.setInCardUserName(userName);
        //拼接查询条件
        criteria.andEqualTo("transferStatus","100");
        criteria.andEqualTo("transferType","102");
        criteria.andEqualTo("inCardUserName",transferRecord.getInCardUserName());
        //获取相关记录
        List<TransferRecord> transferRecordList = transferRecordMapper.selectByExample(example);
        //拼接到ActiveGatheringVo
        List<ActiveGatheringVo> activeGatheringVoList=new ArrayList<ActiveGatheringVo>();
        for (TransferRecord transfer :transferRecordList){
            ActiveGatheringVo activeGatheringVo = new ActiveGatheringVo();
            //收款订单id
            activeGatheringVo.setActiveId(transfer.getTransferRecordId());
            //收款人
            activeGatheringVo.setInUserId(transfer.getUserId());
            BankUser bankUser=new BankUser();
            bankUser.setUserId(transfer.getUserId());
            //收款人姓名
             bankUser = userMapper.selectByPrimaryKey(bankUser);
            activeGatheringVo.setInUserName(bankUser.getUserName());
            //收款卡
            activeGatheringVo.setInBankCard(StarUtil.StringAddStar(transfer.getBankOutCard(),4,4));
            //收款额
            activeGatheringVo.setMuchMoney(transfer.getTransferRecordAmount());
            //收款备注
            activeGatheringVo.setTransferRemarks(transfer.getTransferNote());
            activeGatheringVoList.add(activeGatheringVo);
        }
        return  activeGatheringVoList;
    }
    /**
     * @author: nwm
     * @param: * agreeGathering
     * @return: * boolean
     * @description: 用户同意付款时执行的方法
     * @data: 2019/8/14 19:00
     */
    public  boolean agreeGathering(ActiveGatheringVo agvo){
        //根据用户传的订单agvo.getActiveId()
        // 银行卡idagvo.getOutBankId()
        // 银行卡密码 agvo.getOutBankPassword()

        //查询 付款卡信息
        BankCard  outCard=new BankCard();
        outCard.setBankCardId(agvo.getOutBankId());
        outCard = bankCardMapper.selectByPrimaryKey(outCard);
        if (outCard==null){
            return false;
        }
        //校验付款卡密码 /未加密
        if(bankCardService.BankCardPasswordCheck(outCard,agvo.getOutBankPassword())){
            return false;
        }
        TransferRecord transferRecord=new TransferRecord();
        //获取前台要修改的交易记录订单id
        transferRecord.setTransferRecordId(new Integer(agvo.getActiveId()));
        transferRecord=transferRecordMapper.selectByPrimaryKey(transferRecord);
        if (transferRecord==null){
            return false;
        }
        //校验付款卡金额是否足够
        BigDecimal  money=outCard.getBankCardBalance().subtract(transferRecord.getTransferRecordAmount());

        if(money.doubleValue()<0){
            return false;
        }
        //查询要修改的订单 获取收款卡
        Example example = new Example(BankCard.class);
        Example.Criteria criteria = example.createCriteria();
        BankCard inCard=new BankCard();
        //根据原订单收款卡号查询收款卡
        criteria.andEqualTo("bankCardNumber",transferRecord.getBankOutCard());
        inCard = bankCardMapper.selectOneByExample(example);
        if(inCard==null){
            return false;
        }
        //付款卡扣钱
        outCard.setBankCardBalance(outCard.getBankCardBalance().subtract(transferRecord.getTransferRecordAmount()));
        //收款卡加钱
        inCard.setBankCardBalance(inCard.getBankCardBalance().add(transferRecord.getTransferRecordAmount()));
        outCard.setGmtModified(new Date());
        inCard.setGmtModified(new Date());
        //银行卡数据修改到数据库
        int outFlag=bankCardMapper.updateByPrimaryKey(outCard);
        if (outFlag<0){
            return false;
        }
       int inFlag=bankCardMapper.updateByPrimaryKey(inCard);
        if (inFlag<0){
            return false;
        }
        //修改订单状态
        transferRecord.setTransferStatus(new Byte("101"));
        transferRecord.setGmtModified(new Date());
        transferRecord.setBankInCard(outCard.getBankCardNumber());
         transferRecord.setBankInIdentification(AccessBank.getSubordinateBank(outCard.getBankCardNumber()));
        int transferFlag=transferRecordMapper.updateByPrimaryKey(transferRecord);
        if (transferFlag<0){
            return false;
        }
        return true;
    }

    /**
     * @author: nwm
     * @param: * getManagerTranscation
     * @return: * HashMap<String,Object>
     * @description: 用户进入消息中心页面时执行的方法,查询相关升级卡申请的
     * @data: 2019/8/13 19:00
     */
    //根据当前登录用户id查询升级卡申请的相关记录
    public  List<ManagerTranscation> getManagerTranscation(Integer userId){
        if (userId==null){
            return null;
        }
        ManagerTranscation managerTranscation=new ManagerTranscation();
        Example example = new Example(ManagerTranscation.class);
        Example.Criteria criteria = example.createCriteria();
        //获取当前登录用户id
        //根据当前登录用户id查询
        managerTranscation.setUserId(userId);
        //查询类型是升级卡申请的
        criteria.andEqualTo("transcationStatus","0");
        criteria.andEqualTo("userId",managerTranscation.getUserId());
        List<ManagerTranscation> managerTranscations = managerTranscationMapper.selectByExample(example);
            for (ManagerTranscation manager: managerTranscations){
                manager.setBankCard(StarUtil.StringAddStar(manager.getBankCard(),4,4));

            }
        return managerTranscations;
    }
    /**
     * @author: nwm
     * @param: * updateManagerTranscationStatus
     * @return: * HashMap<String,Object>
     * @description: 用户取消升级卡申请的方法
     * @data: 2019/8/13 19:00
     */

    public  boolean updateManagerTranscationStatus(String transcationId){
        //根据事务表id取消
        ManagerTranscation managerTranscation=managerTranscationMapper.selectByPrimaryKey(transcationId);
        managerTranscation.setTranscationStatus(Byte.parseByte("2"));
        managerTranscation.setGmtModified(new Date());
       int status=managerTranscationMapper.updateByPrimaryKeySelective(managerTranscation);
        if (status > 0) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * @author: nwm
     * @param: * getBankCardByUser
     * @return: * List<BankCard>
     * @description: 根据登录的用户查询用户名下的银行卡
     * @data: 2019/8/13 19:00
     */
    public   List<BankCard>  getBankCardByUser(Integer userId){
        BankCard  bankCard=new BankCard();
        //通过用户id查询用户绑定银行卡 只查询id 和卡号 类型   要求状态为正常的卡
        bankCard.setUserId(userId);
        Example example = new Example(BankCard.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", bankCard.getUserId());
        criteria.andEqualTo("bankCardStatus",100);
        List<BankCard> bankCards = bankCardMapper.selectByExample(example);
        for (BankCard bank : bankCards) {
            //通过银行卡标识获取银行卡名字
            bank.setBank(getBankName(bank.getBankCardNumber()));
            bank.setBankCardNumber(StarUtil.StringAddStar(bank.getBankCardNumber(),4,4));
            bank.setBankCardPassword("");
            bank.setUserId(null);
            bank.setBankCardBalance(null);
            bank.setBankCardPhone("");
            bank.setBankCardTransferLimit(null);
            bank.setGmtCreate(null);
            bank.setGmtCreate(null);
        }
        return bankCards;
    }
        public String getBankName(String bankCardNumber){
            String subordinateBanksIdentification=AccessBank.getSubordinateBank(bankCardNumber);
            if (subordinateBanksIdentification==null ||"".equals(subordinateBanksIdentification)){
                return "";
            }
            Example example2 =new Example(SubordinateBank.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("bankIdentification",subordinateBanksIdentification);
            SubordinateBank subordinateBank = subordinateBankMapper.selectOneByExample(example2);
            return subordinateBank.getBankName();
        }
}
