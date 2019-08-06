package com.zl.dc.service;

import com.zl.dc.mapper.BankManagerMapper;
import com.zl.dc.mapper.ManagerTranscationMapper;
import com.zl.dc.mapper.TransferRecordMapper;
import com.zl.dc.mapper.UserMapper;
import com.zl.dc.pojo.BankManager;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.ManagerTranscation;
import com.zl.dc.pojo.TransferRecord;
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
    private UserMapper userMapper;
     @Resource
     private TransferRecordMapper transferRecordMapper;
     @Resource
     private ManagerTranscationMapper managerTranscationMapper;
     @Resource
     private BankManagerMapper bankManagerMapper;
    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankUser>
     * @description: 查询会员数
     * @data: 2019/8/6 14:04
     */
    public List<BankUser> GetUserList() {
        return userMapper.selectAll();
    }
    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.TransferRecord>
     * @description: 查询记录数
     * @data: 2019/8/6 14:03
     */
    public List<TransferRecord> GetRecords() {
        return transferRecordMapper.selectAll();
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
     * @description: 功能描述
     * @data: 2019/8/6 14:43
     */
    public BankManager getLogin(String managerName) {
        //创建条件
        Example example = new Example(BankManager.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("managerName",managerName);
        return bankManagerMapper.selectOneByExample(example);
    }



}
