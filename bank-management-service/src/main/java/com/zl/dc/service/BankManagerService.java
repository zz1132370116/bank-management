package com.zl.dc.service;

import com.alibaba.fastjson.JSONObject;
import com.zl.dc.mapper.*;
import com.zl.dc.pojo.*;
import com.zl.dc.util.AccessBank;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
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

    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankUser>
     * @description: 查询会员数
     * @data: 2019/8/6 14:04
     */
    public List<BankUser> GetUserList() {
        return bankUserMapper.selectAll();
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
    public List<TransferRecord> getRecordsByParams(String idCard, Date startDate, Date endDate) {
        //创建空对象
        TransferRecord transferRecord = new TransferRecord();
        //创建银行卡条件
        Example example = new Example(SubordinateBank.class);
        Example.Criteria criteria = example.createCriteria();
        //创建记录条件
        Example example1 = new Example(TransferRecord.class);
        Example.Criteria criteria1 = example1.createCriteria();
        //非空判断
        if (!idCard.equals("")) {
            //拼接条件
            criteria1.andEqualTo("idCard",idCard);
        } else if (startDate != null) {
            //拼接条件
            criteria1.andGreaterThan("gmt_create",startDate);
        } else if (endDate != null) {
            //拼接条件
            criteria1.andLessThan("gmt_create",endDate);
        }
        //条件查询
        List<TransferRecord> transferRecords = transferRecordMapper.selectByExample(example1);
        //遍历
        for (TransferRecord record : transferRecords) {
            //通过用户id查询当前用户信息
            BankUser bankUser = bankUserMapper.selectByPrimaryKey(record.getUserId());
            //处理身份证号
            String idCards = handlingIdCards(bankUser.getIdCard());
            bankUser.setIdCard(idCards);
            //赋值
            record.setBankUser(bankUser);
            //获取转出卡所属银行
            String bankOut = AccessBank.getCardDetail(record.getBankOutCard());
            JSONObject jsonOut = JSONObject.parseObject(bankOut);
            criteria.andEqualTo("bankIdentification", jsonOut.get("bank"));
            SubordinateBank subordinateBank = subordinateBankMapper.selectOneByExample(example);
            record.setBankOutCardName(subordinateBank.getBankName());
            //获取转入卡所属银行
            String bankIn = AccessBank.getCardDetail(record.getBankInCard());
            JSONObject jsonIn = JSONObject.parseObject(bankIn);
            criteria.andEqualTo("bankIdentification", jsonIn.get("bank"));
            SubordinateBank selectOneByExample = subordinateBankMapper.selectOneByExample(example);
            record.setBankInCardName(selectOneByExample.getBankName());
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
    public String handlingIdCards(String idcard){
        //处理身份证号
        StringBuilder sb = new StringBuilder(idcard);
        sb.replace(6, 14, "****");
        return sb.toString();
    }
}
