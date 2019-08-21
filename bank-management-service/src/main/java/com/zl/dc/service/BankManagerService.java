package com.zl.dc.service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.zl.dc.config.SendUpgradeCardOK;
import com.zl.dc.mapper.*;
import com.zl.dc.pojo.*;
import com.zl.dc.util.StarUtil;
import org.apache.commons.lang3.StringUtils;
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
    @Resource
    private BankCardMapper bankCardMapper;

    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankUser>
     * @description: 查询会员数
     * @data: 2019/8/6 14:04
     */
    public List<BankUser> GetUserList(Integer pageNum) {
        List<BankUser> users = bankUserMapper.GetUserList(pageNum);

        for (BankUser user : users) {
            if (user.getIdCard()!=null && !user.getIdCard().equals("")) {

                String s = handlingIdCards(user.getIdCard());
                user.setIdCard(s);

            }
        }
        return users;
    }

    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.TransferRecord>
     * @description: 查询记录
     * @data: 2019/8/6 14:03
     */
    public List<TransferRecord> GetRecords(Integer pageNum) {
        Example example3 = new Example(SubordinateBank.class);
        Example.Criteria criteria3 = example3.createCriteria();
        List<TransferRecord> transferRecords = transferRecordMapper.GetRecords(pageNum);
        for (TransferRecord transferRecord : transferRecords) {
            //通过用户id查询当前用户信息
            BankUser bankUser = bankUserMapper.selectByPrimaryKey(transferRecord.getUserId());
            //处理身份证号
            if (bankUser.getIdCard()!=null && !bankUser.getIdCard().equals("")){
                String idCards = handlingIdCards(bankUser.getIdCard());
                bankUser.setIdCard(idCards);
            }

            transferRecord.setUserName(bankUser.getUserName());
    if (transferRecord.getBankInIdentification() !=null && !transferRecord.getBankInIdentification().equals("")){

        criteria3.andEqualTo("bankIdentification", transferRecord.getBankInIdentification());
        transferRecord.setBankOutCardName("五仁银行");
    }
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
    public List<ManagerTranscation> GetAbnormals(Integer pageNum) {
        List<ManagerTranscation> list = managerTranscationMapper.getManagerTranscations(pageNum);
        return list;
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
        Example example3 = new Example(SubordinateBank.class);
        Example.Criteria criteria3 = example3.createCriteria();
        //非空判断
        if (StringUtils.isNotBlank(transferRecord.getIdCard())&& transferRecord.getStartDate() !=null && transferRecord.getEndDate()!=null){
            List<TransferRecord> transferRecords = transferRecordMapper.getRecordsByParams(transferRecord.getIdCard(),transferRecord.getStartDate(),transferRecord.getEndDate(),transferRecord.getPageNum());
            for (TransferRecord record : transferRecords) {
                BankUser bankUser = bankUserMapper.selectByPrimaryKey(record.getUserId());
                //赋值
                record.setBankOutCard(StarUtil.StringAddStar(record.getBankOutCard(),6,4));
                record.setBankInCard(StarUtil.StringAddStar(record.getBankInCard(),6,4));
                record.setUserName(bankUser.getUserName());
                //获取转入卡所属银行
                criteria3.andEqualTo("bankIdentification", record.getBankInIdentification());
                record.setBankOutCardName("五仁银行");
                //获取转入卡所属银行
                record.setBankInCardName(subordinateBankMapper.selectOneByExample(example3).getBankName());
            }
            return transferRecords;
        }

        return null;
    }

    /**
     * @author: zhanglei
     * @param: [idcard]
     * @return:java.lang.String
     * @description: 处理身份证号码
     * @data: 2019/8/7 11:25
     */
    public static String handlingIdCards(String idcard) {
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
    public List<BankUser> getUserListByParams(String userName, String idCard,Integer pageNum) {
        if (StringUtils.isNotBlank(idCard) && StringUtils.isNotBlank(userName)){
            List<BankUser> userListByParams = bankUserMapper.getUserListByParams(userName, idCard, pageNum);
            for (BankUser userListByParam : userListByParams) {

                userListByParam.setIdCard( handlingIdCards(userListByParam.getIdCard()));
            }
            return userListByParams;
        }else if (StringUtils.isNotBlank(userName)){
            List<BankUser> bankUsers =bankUserMapper.getUserListByUserName(userName,pageNum);
            for (BankUser userListByParam : bankUsers) {
                if (!userListByParam.getIdCard().equals("") && userListByParam.getIdCard()!=null){
                    userListByParam.setIdCard( handlingIdCards(userListByParam.getIdCard()));
                }

            }
            return bankUsers;
        }else if(StringUtils.isNotBlank(idCard)){
            List<BankUser> bankUsers =bankUserMapper.getUserListByIDCARD(idCard,pageNum);
            for (BankUser userListByParam : bankUsers) {
                if (!userListByParam.getIdCard().equals("") && userListByParam.getIdCard()!=null){
                    userListByParam.setIdCard( handlingIdCards(userListByParam.getIdCard()));
                }
            }
            return bankUsers;
        }

        return null;
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
    public List<ManagerTranscation> getManagerTranscations(Integer pageNum) {
        List<ManagerTranscation> list =  managerTranscationMapper.getManagerTranscations(pageNum);
        if (list !=null){
            for (ManagerTranscation managerTranscation : list) {
                if (managerTranscation.getUserId() !=null){
                    BankUser bankUser = bankUserMapper.selectByPrimaryKey(managerTranscation.getUserId());
                            managerTranscation.setBankUser(bankUser);
                    if (bankUser.getIdCard()!=null && !bankUser.getIdCard().equals("")){
                        managerTranscation.getBankUser().setIdCard(handlingIdCards(managerTranscation.getBankUser().getIdCard()));
                    }
                }
            }
        }

        return list;
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
        Example example = new Example(BankCard.class);
        Example.Criteria criteria = example.createCriteria();
        //修改银行卡类型
        //通过主键查询事务
        ManagerTranscation managerTranscation1 = managerTranscationMapper.selectByPrimaryKey(transcationId);
        managerTranscation1.setTranscationStatus(Byte.parseByte("1"));
        managerTranscation1.setGmtModified(new Date());
        managerTranscation1.setManagerId(2);
        managerTranscationMapper.updateByPrimaryKeySelective(managerTranscation1);
        criteria.andEqualTo("bankCardNumber",managerTranscation1.getBankCard());
        BankCard bankCard = bankCardMapper.selectOneByExample(example);
        //如果类型是普通卡则升级为钻石卡
        if (bankCard.getBankCardType().equals("普通卡")) {
            bankCard.setBankCardType("钻石卡");
        }else if ("钻石卡".equals(bankCard.getBankCardType())) {
            //如果类型是钻石卡则升级为黑卡
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
        //创建事务实体类
        Example example = new Example(BankCard.class);
        Example.Criteria criteria = example.createCriteria();
        //通过主键查询事务
        ManagerTranscation managerTranscation1 = managerTranscationMapper.selectByPrimaryKey(transcationId);

        managerTranscation1.setTranscationStatus(Byte.parseByte("2"));
        managerTranscation1.setGmtModified(new Date());
        managerTranscation1.setManagerId(2);
        managerTranscationMapper.updateByPrimaryKeySelective(managerTranscation1);

    }

     /**
      * @author: zhanglei
      * @param:
      * @return:
      * @description: 查询事务数量
      * @data: 2019/8/21 14:35
      */
    public String selectManagerTranscationAll() {
        Integer selectcount = managerTranscationMapper.selectcount();
        return selectcount.toString();
    }
     /**
      * @author: zhanglei
      * @param:
      * @return:
      * @description: 查询转账记录数量
      * @data: 2019/8/21 14:35
      */
    public String selectTransferRecordAll() {
        Integer selectcount = transferRecordMapper.selectcount();
        return selectcount .toString();
    }
     /**
      * @author: zhanglei
      * @param:
      * @return:
      * @description: 查询用户数量
      * @data: 2019/8/21 14:35
      */
    public String  selectBankUserAll() {
        Integer i = bankUserMapper.selectcount();
        return i.toString();
    }
}
