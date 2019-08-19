package com.zl.dc.service;

import com.zl.dc.mapper.BankCardDOMapper;
import com.zl.dc.mapper.TransferRecordDOMapper;
import com.zl.dc.mapper.TransferRecordMapper;
import com.zl.dc.mapper.UserMapper;
import com.zl.dc.pojo.TransferRecord;
import com.zl.dc.util.StarUtil;
import com.zl.dc.vo.PageBean;
import com.zl.dc.vo.TransferValueVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

/**
 * @version: V1.2
 * @author: lu
 * @className: TransferRecordService
 * @description: 转账记录操作层
 * @data: 2019/8/12
 */
@Service
@Transactional
public class TransferRecordService {
    @Resource
    private TransferRecordMapper transferRecordMapper;
    @Resource
    private TransferRecord transferRecord;
    @Resource
    private TransferRecordDOMapper transferRecordDOMapper;
    @Resource
    private PageBean pageBean;
    @Resource
    private BankCardDOMapper bankCardDOMapper;
    @Resource
    private UserMapper userMapper;

    /**
     * @author: lu
     * @param: * TransferValueVo
     * @return: * boolean
     * @description: 根据单次转账VO添加转账记录
     * @data: 2019/8/12 15:57
     */
    public TransferRecord addTransferRecordforTransferValueVo(TransferValueVo transferValueVo) {
        if (transferValueVo == null) {
            return null;
        }
        if (transferValueVo.getMuchMoney() == null) {
            return null;
        }
        if (StringUtils.isBlank(transferValueVo.getTransferRemarks())) {
            return null;
        }
        if (transferValueVo.getUserId() == null) {
            return null;
        }
        if (StringUtils.isBlank(transferValueVo.getOutBankCard())) {
            return null;
        }
        if (StringUtils.isBlank(transferValueVo.getInBankName())) {
            return null;
        }
        if (StringUtils.isBlank(transferValueVo.getInBank())) {
            return null;
        }
        if (StringUtils.isBlank(transferValueVo.getInBankCard())) {
            return null;
        }
        transferRecord.setTransferRecordUuid(UUID.randomUUID().toString().replaceAll("-", ""));
        transferRecord.setTransferRecordAmount(transferValueVo.getMuchMoney());
        transferRecord.setTransferRecordTime(null);
        transferRecord.setTransferNote(transferValueVo.getTransferRemarks());
        transferRecord.setTransferType(Byte.parseByte("100"));
        transferRecord.setTransferStatus(Byte.parseByte("110"));
        transferRecord.setUserId(transferValueVo.getUserId());
        transferRecord.setBankOutCard(transferValueVo.getOutBankCard());
        transferRecord.setInCardUserName(transferValueVo.getInBankName());
        transferRecord.setBankInIdentification(transferValueVo.getInBank());
        transferRecord.setBankInCard(transferValueVo.getInBankCard());
        transferRecord.setGmtCreate(new Date());
        transferRecord.setGmtModified(new Date());
        //添加转账记录
        transferRecordMapper.insertSelective(transferRecord);
        //拼接查询条件
        return selectTransferRecordByUuid(transferRecord.getTransferRecordUuid());


    }

    /**
     * @author: lu
     * @Param String uuid
     * @return: TransferRecord
     * @description: 根据uuid查询转账记录
     * @data: 2019/8/14 19:05
     */
    public TransferRecord selectTransferRecordByUuid(String uuid) {
        Example example = new Example(TransferRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("transferRecordUuid", uuid);
        return transferRecordMapper.selectOneByExample(example);
    }

    /**
     * @author: lu
     * @param: * TransferRecord
     * @return: * boolean
     * @description: 转账失败时修改转账记录的方法
     * @data: 2019/8/12 17:00
     */
    public boolean transferFailedOperation(TransferRecord transferRecord) {
        transferRecord.setTransferStatus(Byte.parseByte("130"));
        transferRecord.setTransferRecordTime(new Date());
        transferRecord.setGmtModified(new Date());
        int status = transferRecordMapper.updateByPrimaryKeySelective(transferRecord);
        if (status > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @author: lu
     * @param: * TransferRecord
     * @return: * boolean
     * @description: 转账成功时时修改转账记录的方法
     * @data: 2019/8/12 17:00
     */
    public boolean transferSuccessfulOperation(TransferRecord transferRecord) {
        transferRecord.setTransferStatus(Byte.parseByte("120"));
        transferRecord.setTransferRecordTime(new Date());
        transferRecord.setGmtModified(new Date());
        int status = transferRecordMapper.updateByPrimaryKeySelective(transferRecord);
        if (status > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @author: Redsheep
     * @Param pageBean
     * @Param month
     * @Param bankCardId
     * @return: java.util.List<com.zl.dc.pojo.TransferRecord>
     * @description: 转账记录的分页查询
     * @data: 2019/8/13 17:23
     */
    public List<TransferRecord> getTransferRecordList(Integer page, Integer month, Integer userId, Integer bankCardId) {
        // 查询银行卡号
        String bankCard;
        if (bankCardId == 0) {
            bankCard = "";
        } else {
            bankCard = bankCardDOMapper.selectBankCardNumberById(bankCardId);
        }
        // 分页封装处理
        pageBean.setPage(page);
        pageBean.setPageSize(5);
        pageBean.setIndex((pageBean.getPage() - 1) * pageBean.getPageSize());
        // 日期字符串的处理
        StringBuilder startDay = new StringBuilder("2019");
        StringBuilder endDay = new StringBuilder("2019");
        if (month <= 9) {
            startDay.append("0").append(month).append("01");
            if (month == 9) {
                endDay.append(month + 1).append("01");
            } else {
                endDay.append("0").append(month + 1).append("01");
            }
        } else {
            startDay.append(month).append("01");
            endDay.append(month + 1).append("01");
        }
        // 查询语句
        List<TransferRecord> transferRecordList = transferRecordDOMapper.selectByUserIdAndMonthAndCard(userId, startDay.toString(), endDay.toString(), pageBean.getIndex(), pageBean.getPageSize(), bankCard);
        /**
         * 查询结果的处理
         * 1.收款银行卡加*
         * 2.状态和类型显意
         */
        ListIterator<TransferRecord> iterator = transferRecordList.listIterator();
        TransferRecord transferRecord;
        String status;
        String type;
        String bankOutCard;
        while (iterator.hasNext()) {
            transferRecord = iterator.next();
            status = changeTransferStatus(transferRecord.getTransferStatus().toString());
            transferRecord.setTransferStringStatus(status);
            type = changeTransferType(transferRecord.getTransferType().toString());
            transferRecord.setTransferStringType(type);
            bankOutCard = transferRecord.getBankOutCard();
            transferRecord.setBankOutCard(StarUtil.StringAddStar(bankOutCard, 4, 4));
        }
        return transferRecordList;
    }

    /**
     * @author: Redsheep
     * @Param planId 归集计划id
     * @return: java.util.List<com.zl.dc.pojo.TransferRecord>
     * @description: 归集计划记录
     * @data: 2019/8/19 9:36
     */
    public List<TransferRecord> getFundCollectionRecordList(Integer planId) {
        List<TransferRecord> transferRecordList = transferRecordDOMapper.selectFundCollectionRecord(planId);
        if (transferRecordList == null) {
            return null;
        }
        ListIterator<TransferRecord> transferRecordListIterator = transferRecordList.listIterator();
        TransferRecord transferRecord;
        String type;
        while (transferRecordListIterator.hasNext()) {
            transferRecord = transferRecordListIterator.next();
            type = changeTransferStatus(transferRecord.getTransferStatus().toString());
            transferRecord.setTransferStringStatus(type);
        }
        return transferRecordList;
    }

    /**
     * @author: Redsheep
     * @Param type
     * @return: java.lang.String
     * @description: 转账类型显义
     * @data: 2019/8/13 17:48
     */
    private String changeTransferType(String type) {
        switch (type) {
            case "100":
                return "单次转账";
            case "101":
                return "批量转账";
            case "102":
                return "主动收款";
            case "103":
                return "跨境转账";
            case "104":
                return "手机转账";
            case "105":
                return "企业转个人";
            case "106":
                return "个人转企业";
            case "107":
                return "资金归集";
            default:
                return "未知";
        }
    }

    /**
     * @author: Redsheep
     * @Param status
     * @return: java.lang.String
     * @description: 交易状态显义
     * @data: 2019/8/13 17:48
     */
    private String changeTransferStatus(String status) {
        switch (status) {
            case "100":
                return "交易中";
            case "101":
                return "成功";
            case "102":
                return "失败";
            default:
                return "未知";
        }
    }


}
