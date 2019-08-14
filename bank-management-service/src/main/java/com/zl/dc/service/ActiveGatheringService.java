package com.zl.dc.service;


import com.zl.dc.mapper.BankCardMapper;
import com.zl.dc.mapper.ManagerTranscationMapper;
import com.zl.dc.mapper.TransferRecordMapper;
import com.zl.dc.pojo.BankCard;

import com.zl.dc.pojo.ManagerTranscation;
import com.zl.dc.pojo.TransferRecord;
import com.zl.dc.vo.ActiveGatheringVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
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
    //转账记录实体类
    @Resource
    private TransferRecord transferRecord;
    //管理员事务表Mapper
    @Resource
    private ManagerTranscationMapper managerTranscationMapper;
    //管理员事务表实体类
    @Resource
    private ManagerTranscation managerTranscation;
    //银行卡Mapper
    @Resource
    private BankCardMapper bankCardMapper;
    //银行卡实体类
    @Resource
    private BankCard bankCard;
    //session对象
    @Autowired
    private HttpSession session;


    /**
     * @author: nwm
     * @param: * getActiveGatheringVoList
     * @return: * boolean
     * @description: 查询所有主动收款记录
     * @data: 2019/8/13 19:00
     */
    public List<ActiveGatheringVo> getActiveGatheringVoList() {
        Example example = new Example(TransferRecord.class);
        Example.Criteria criteria = example.createCriteria();
        //从session中获取当前登录用户id
        Integer userId=null;
        transferRecord.setUserId(userId);
        //拼接查询条件
        criteria.andEqualTo("transferType","130");
        criteria.andEqualTo("userId",transferRecord.getUserId());
        //获取相关记录
        List<TransferRecord> transferRecordList = transferRecordMapper.selectByExample(example);


        //拼接到ActiveGatheringVo
        List<ActiveGatheringVo> activeGatheringVoList=new ArrayList<ActiveGatheringVo>();
        ActiveGatheringVo activeGatheringVo = new ActiveGatheringVo();

        for (TransferRecord transferRecord :transferRecordList){
            //付款人姓名
            activeGatheringVo.setOutUserName(transferRecord.getInCardUserName());
            //收款卡
            activeGatheringVo.setInBankCard(transferRecord.getBankOutCard());
            //收款额
            activeGatheringVo.setMuchMoney(transferRecord.getTransferRecordAmount());
            //收款备注
            activeGatheringVo.setTransferRemarks(transferRecord.getTransferNote());
            //收款状态
            activeGatheringVo.setActivestate(new Integer(transferRecord.getTransferNote()));
            activeGatheringVoList.add(activeGatheringVo);
        }
         return activeGatheringVoList;
    }

    /**
     * @author: nwm
     * @param: * updateGatheringStatus
     * @return: * HashMap<String,Object>
     * @description: 用户修改主动收款状态为取消时执行的方法,及付款用户拒绝付款
     * @data: 2019/8/13 19:00
     */
    public boolean updateGatheringStatus(String activeId){
        //从session中获取当前登录用户id
        Integer userId=null;
        transferRecord.setUserId(userId);
        //获取前台要修改的交易记录订单id
        transferRecord.setTransferRecordId(new Integer(activeId));
        //修改交易订单id的状态为取消
        transferRecord.setTransferStatus(Byte.parseByte("130"));

        transferRecord.setTransferRecordTime(new Date());
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
     * @return: * HashMap<String,Object>
     * @description: 用户添加主动收款时执行的方法
     * @data: 2019/8/13 19:00
     */
    public boolean addTransactionTecord(String userPhone,String userBankId , ActiveGatheringVo agvo){
        //根据发起主动收款用户填写的数据添加交易订单
        //agvo 收款订单基本信息
        if(userPhone==null || userPhone.equals("")){
            return  false;
        }
        if(userBankId==null || userBankId.equals("")){
            return  false;
        }
        if(agvo.getMuchMoney()==null || "".equals(agvo.getMuchMoney())){
            return  false;
        }
        if(agvo.getTransferRemarks()==null || "".equals(agvo.getTransferRemarks())){
            return  false;
        }
        //添加自动生成的交易流水号
        transferRecord.setTransferRecordUuid(UUID.randomUUID().toString().replaceAll("-", ""));
        //添加交易额
        transferRecord.setTransferRecordAmount(agvo.getMuchMoney());
        //添加交易时间
        transferRecord.setTransferRecordTime(new Date());
        //添加交易备注
        transferRecord.setTransferNote(agvo.getTransferRemarks());
        //添加交易类型为主动收款
        transferRecord.setTransferType(Byte.parseByte("130"));
        //添加交易状态为交易中
        transferRecord.setTransferStatus(Byte.parseByte("110"));

        //从session中获取当前登录用户id
        Integer userId=null;
        //添加收款用户id
        transferRecord.setUserId(userId);
        /*根据收款卡id 查询收款卡
        * userBankId */
        //添加收款卡
        transferRecord.setBankOutCard(null);


        /*根据付款人手机号查询付款人姓名
            userPhone 付款人电话*/
        //添加付款人姓名
        transferRecord.setInCardUserName(null);
        //付款卡标识暂时为空
        transferRecord.setBankInIdentification(null);
        //付款卡暂时为空
        transferRecord.setBankInCard(null);

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
     * @description: 用户进入消息中心页面时执行的方法,查询相关主动收款
     * @data: 2019/8/13 19:00
     */
    //获取相关待付款记录
    //getActiveGatheringVo
    public List<ActiveGatheringVo> getActiveGatheringVo(){
        Example example = new Example(TransferRecord.class);

        Example.Criteria criteria = example.createCriteria();

        //从session中获取当前登录用户姓名
        String userName=null;
        //查询付款人是当前登录用户的
        transferRecord.setInCardUserName(userName);
        //拼接查询条件
        criteria.andEqualTo("transferStatus","110");
        criteria.andEqualTo("transferType","130");
        criteria.andEqualTo("inCardUserName",transferRecord.getInCardUserName());
        //获取相关记录
        List<TransferRecord> transferRecordList = transferRecordMapper.selectByExample(example);



        //拼接到ActiveGatheringVo
        List<ActiveGatheringVo> activeGatheringVoList=new ArrayList<ActiveGatheringVo>();
        ActiveGatheringVo activeGatheringVo = new ActiveGatheringVo();

        for (TransferRecord transferRecord :transferRecordList){
            //收款人
            activeGatheringVo.setInUserId(transferRecord.getUserId());
            //收款卡
            activeGatheringVo.setInBankCard(transferRecord.getBankOutCard());
            //收款额
            activeGatheringVo.setMuchMoney(transferRecord.getTransferRecordAmount());
            //收款备注
            activeGatheringVo.setTransferRemarks(transferRecord.getTransferNote());
            activeGatheringVoList.add(activeGatheringVo);
        }

        return  activeGatheringVoList;
    }



    /**
     * @author: nwm
     * @param: * getManagerTranscation
     * @return: * HashMap<String,Object>
     * @description: 用户进入消息中心页面时执行的方法,查询相关升级卡申请的
     * @data: 2019/8/13 19:00
     */
    //根据当前登录用户id查询升级卡申请的相关记录
    public  List<ManagerTranscation> getManagerTranscation(){
        Example example = new Example(ManagerTranscation.class);

        Example.Criteria criteria = example.createCriteria();
        //从session中获取当前登录用户id
        Integer userId=null;
        //根据当前登录用户id查询
        managerTranscation.setUserId(userId);
        //查询类型是升级卡申请的
        criteria.andEqualTo("transcationStatus","0");
        criteria.andEqualTo("userId",managerTranscation.getUserId());
        List<ManagerTranscation> managerTranscations = managerTranscationMapper.selectByExample(example);
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
        //获取用户要取消申请的订单
        managerTranscation.setTranscationId(new Integer(transcationId));
        //设置订单状态为取消
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
    public   List<BankCard>  getBankCardByUser(){
        //session中获取当前登录用户id
        Integer uesrId=null;
        //通过用户id查询用户绑定银行卡
        bankCard.setUserId(1);
        Example example = new Example(BankCard.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", bankCard.getUserId());
        List<BankCard> bankCards = bankCardMapper.selectByExample(example);
        for (BankCard bankCard : bankCards) {
            bankCard.setBank("五仁银行");
        }
        //相当于where
        return bankCards;
    }

}
