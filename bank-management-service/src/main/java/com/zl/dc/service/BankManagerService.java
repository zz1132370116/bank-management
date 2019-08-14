package com.zl.dc.service;

import com.zl.dc.mapper.*;
import com.zl.dc.pojo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version: V1.0
 * @author: zhanglei
 * @className: BankManagerService
 * @description: 管理员操作层
 * @data: 2019/8/6 13:48
 */
@Service
@Transactional
public class BankManagerService {
    @Resource
    private UserMapper bankUserMapper;
    @Resource
    private TransferRecordMapper transferRecordMapper;
    @Resource
    private ManagerTranscationMapper managerTranscationMapper;
    @Resource
    private BankManagerMapper bankManagerMapper;
    @Resource
    private SubordinateBankMapper subordinateBankMapper;
    @Resource
    private BankCardMapper bankCardMapper;

    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankUser>
     * @description: 查询会员数
     * @data: 2019/8/6 14:04
     */
    public List<BankUser> GetUserList() {
        List<BankUser> users = bankUserMapper.selectAll();
        for (BankUser user : users) {
            String s = handlingIdCards(user.getIdCard());
            user.setIdCard(s);
        }
        return users;
    }

    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.TransferRecord>
     * @description: 查询记录数
     * @data: 2019/8/6 14:03
     */
    public List<TransferRecord> GetRecords() {

        List<TransferRecord> transferRecords = transferRecordMapper.selectAll();
        for (TransferRecord transferRecord : transferRecords) {
            //通过用户id查询当前用户信息
            BankUser bankUser = bankUserMapper.selectByPrimaryKey(transferRecord.getUserId());
            //处理身份证号
            String idCards = handlingIdCards(bankUser.getIdCard());
            bankUser.setIdCard(idCards);
            transferRecord.setUserName(bankUser.getUserName());
            Example example3 = new Example(SubordinateBank.class);
            Example.Criteria criteria3 = example3.createCriteria();
            criteria3.andEqualTo("bankIdentification", transferRecord.getBankInIdentification());
            transferRecord.setBankOutCardName("五仁银行");
            //获取转入卡所属银行
            transferRecord.setBankInCardName(subordinateBankMapper.selectOneByExample(example3).getBankName());
        }
        return transferRecords;
    }

    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.ManagerTranscation>
     * @description: 查询异常数
     * @data: 2019/8/6 14:03
     */
    public List<ManagerTranscation> GetAbnormals() {
        return managerTranscationMapper.selectAll();
    }

    /**
     * @author: zhanglei
     * @param: [bankManager]
     * @return:com.zl.dc.pojo.BankManager
     * @description: 根据用户名查询
     * @data: 2019/8/6 14:43
     */
    public BankManager getLogin(String managerName) {
        //创建条件
        Example example = new Example(BankManager.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("managerName", managerName);
        return bankManagerMapper.selectOneByExample(example);
    }

    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankManager>
     * @description: 通过redis查询管理员信息
     * @data: 2019/8/6 15:27
     */
    public List<BankManager> GetUserByRedis() {
        return bankManagerMapper.selectAll();
    }

    /**
     * @author: zhanglei
     * @param: [idCard, startDate, endDate]
     * @return:java.util.List<com.zl.dc.pojo.TransferRecord>
     * @description: 条件查询记录
     * @data: 2019/8/6 19:18
     */
    public List<TransferRecord> getRecordsByParams(TransferRecord transferRecord) {
        //创建记录条件
        Example example1 = new Example(TransferRecord.class);
        Example.Criteria criteria1 = example1.createCriteria();
        Example example2 = new Example(BankUser.class);
        Example.Criteria criteria2 = example2.createCriteria();
        Example example3 = new Example(SubordinateBank.class);
        Example.Criteria criteria3 = example3.createCriteria();
        //非空判断
        if (!transferRecord.getIdCard().equals("")) {
            criteria2.andEqualTo("idCard", transferRecord.getIdCard());
            BankUser bankUser = bankUserMapper.selectOneByExample(example2);
            //拼接条件
            criteria1.andEqualTo("userId", bankUser.getUserId());
        } else if (transferRecord.getStartDate() != null) {
            //拼接条件
            criteria1.andGreaterThan("gmt_create", transferRecord.getStartDate());
        } else if (transferRecord.getEndDate() != null) {
            //拼接条件
            criteria1.andLessThan("gmt_create", transferRecord.getEndDate());
        }
        //条件查询
        List<TransferRecord> transferRecords = transferRecordMapper.selectByExample(example1);
        //遍历
        for (TransferRecord record : transferRecords) {
            //通过用户id查询当前用户信息
            BankUser bankUser = bankUserMapper.selectByPrimaryKey(record.getUserId());
            //赋值
            record.setUserName(bankUser.getUserName());
            //获取转出卡所属银行
            criteria3.andEqualTo("bankIdentification", record.getBankInIdentification());
            record.setBankOutCardName("五仁银行");
            //获取转入卡所属银行
            record.setBankInCardName(subordinateBankMapper.selectOneByExample(example3).getBankName());
        }
        return transferRecords;
    }

    /**
     * @author: zhanglei
     * @param: [idcard]
     * @return:java.lang.String
     * @description: 处理身份证号码
     * @data: 2019/8/7 11:25
     */
    public String handlingIdCards(String idcard) {
        //处理身份证号
        StringBuilder sb = new StringBuilder(idcard);
        sb.replace(6, 14, "****");
        return sb.toString();
    }

    /**
     * @author: zhanglei
     * @param: [userName, idCard]
     * @return:java.util.List<com.zl.dc.pojo.BankUser>
     * @description: 条件查询所有用户
     * @data: 2019/8/7 14:39
     */
    public List<BankUser> getUserListByParams(String userName, String idCard) {
        Example example = new Example(BankUser.class);
        Example.Criteria criteria = example.createCriteria();
        if (userName != null && !"".equals(userName)) {
            criteria.andEqualTo("userName", userName);
        } else if (idCard != null && !"".equals(idCard)) {
            criteria.andEqualTo("idCard", idCard);
        }
        List<BankUser> users = bankUserMapper.selectByExample(example);
        for (BankUser user : users) {
            String s = handlingIdCards(user.getIdCard());
            user.setIdCard(s);

        }
        return users;
    }

    /**
     * @author: zhanglei
     * @param: [userId]
     * @return:void
     * @description: 修改用户状态(启用)
     * @data: 2019/8/7 14:57
     */
    public void memberStart(Integer userId) {

        BankUser bankUser = new BankUser();
        bankUser.setUserId(userId);
        bankUser.setUserStatus(Byte.parseByte("0"));
        bankUserMapper.updateByPrimaryKeySelective(bankUser);
    }

    /**
     * @author: zhanglei
     * @param: [userId]
     * @return:void
     * @description: 修改用户状态(停用)
     * @data: 2019/8/7 14:57
     */
    public void memberStop(Integer userId) {

        BankUser bankUser = new BankUser();
        bankUser.setUserId(userId);
        bankUser.setUserStatus(Byte.parseByte("1"));
        bankUserMapper.updateByPrimaryKeySelective(bankUser);
    }

    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.ManagerTranscation>
     * @description: 查询用户申请中的提卡信息
     * @data: 2019/8/9 15:03
     */
    public List<ManagerTranscation> getManagerTranscations() {
        Example example = new Example(ManagerTranscation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("transcationStatus", 0);
        List<ManagerTranscation> managerTranscations = managerTranscationMapper.selectByExample(example);
        for (ManagerTranscation managerTranscation : managerTranscations) {
            BankUser bankUser = bankUserMapper.selectByPrimaryKey(managerTranscation.getUserId());
            managerTranscation.setBankUser(bankUser);
        }
        return managerTranscations;
    }

    /**
     * @author: zhanglei
     * @param: [transcationId]
     * @return:void
     * @description: 提卡申请(通过)
     * @data: 2019/8/9 15:17
     */
    public void adopt(Integer transcationId) {
        //创建事务实体类
        ManagerTranscation managerTranscation = new ManagerTranscation();
        managerTranscation.setTranscationId(transcationId);
        managerTranscation.setTranscationStatus(Byte.parseByte("1"));
        //修改状态
        managerTranscationMapper.updateByPrimaryKeySelective(managerTranscation);
        //修改银行卡类型
        //通过主键查询事务
        ManagerTranscation managerTranscation1 = managerTranscationMapper.selectByPrimaryKey(transcationId);
        BankCard bankCard = bankCardMapper.selectByPrimaryKey(managerTranscation1.getBankCard());
        //如果类型是普通卡则升级为钻石卡
        if (bankCard.getBankCardType().equals("普通卡")) {
            bankCard.setBankCardType("钻石卡");
        }
        //如果类型是钻石卡则升级为黑卡
        if ("钻石卡".equals(bankCard.getBankCardType())) {
            bankCard.setBankCardType("黑卡");
        }

        bankCardMapper.updateByPrimaryKeySelective(bankCard);
    }

    /**
     * @author: zhanglei
     * @param: [transcationId]
     * @return:void
     * @description: 提卡申请(不通过)
     * @data: 2019/8/9 15:17
     */
    public void NoPassage(Integer transcationId) {
        ManagerTranscation managerTranscation = new ManagerTranscation();
        managerTranscation.setTranscationId(transcationId);
        managerTranscation.setTranscationStatus(Byte.parseByte("2"));
        managerTranscationMapper.updateByPrimaryKeySelective(managerTranscation);
    }

}
