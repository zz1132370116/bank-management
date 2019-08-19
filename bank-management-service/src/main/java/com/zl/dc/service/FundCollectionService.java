package com.zl.dc.service;

import com.zl.dc.mapper.FundCollectionPlanDOMapper;
import com.zl.dc.mapper.FundCollectionRecordDOMapper;
import com.zl.dc.pojo.FundCollectionPlan;
import com.zl.dc.pojo.FundCollectionRecordDO;
import com.zl.dc.pojo.TransferRecord;
import com.zl.dc.util.StarUtil;
import com.zl.dc.vo.TransferValueVo;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
@EnableScheduling
public class FundCollectionService {
    @Resource
    FundCollectionPlanDOMapper fundCollectionPlanDOMapper;
    @Resource
    FundCollectionRecordDOMapper fundCollectionRecordDOMapper;
    @Resource
    BankCardService bankCardService;
    @Resource
    TransferRecordService transferRecordService;

    @Resource
    FundCollectionRecordDO fundCollectionRecordDO;
    @Resource
    TransferRecord transferRecord;
    @Resource
    FundCollectionPlan fundCollectionPlan;

    /**
     * @author: Redsheep
     * @Param userId
     * @return: java.util.List<com.zl.dc.pojo.FundCollectionPlan>
     * @description: 获取归集计划列表
     * @data: 2019/8/16 15:58
     */
    public List<FundCollectionPlan> getFundCollectionPlanList(Integer userId) {
        List<FundCollectionPlan> fundCollectionPlans = fundCollectionPlanDOMapper.getFundCollectionPlanList(userId);
        if (fundCollectionPlans == null) {
            return null;
        }
        ListIterator<FundCollectionPlan> iterator = fundCollectionPlans.listIterator();
        String status;
        String bankOutCard;
        String bankInCard;
        FundCollectionPlan fundCollectionPlan;
        while (iterator.hasNext()) {
            fundCollectionPlan = iterator.next();
            // 状态转义
            status = fundCollectionPlan.getPlanStatus().toString();
            fundCollectionPlan.setStatusName(switchPlanStatus(status));
            // 通过银行卡id得到银行卡号
            bankOutCard = bankCardService.selectBankCardNumberById(fundCollectionPlan.getBankOutCardId());
            bankInCard = bankCardService.selectBankCardNumberById(fundCollectionPlan.getBankInCardId());
            fundCollectionPlan.setBankOutCard(StarUtil.StringAddStar(bankOutCard, 4, 4));
            fundCollectionPlan.setBankInCard(StarUtil.StringAddStar(bankInCard, 4, 4));
        }
        return fundCollectionPlans;
    }

    /**
     * @author: Redsheep
     * @Param fundCollectionPlan
     * @return: boolean
     * @description: 添加归集计划
     * @data: 2019/8/16 15:58
     */
    public boolean addFundCollectionPlan(FundCollectionPlan fundCollectionPlan) {
        Date date = new Date();
        fundCollectionPlan.setPlanStatus(Byte.parseByte("100"));
        fundCollectionPlan.setFailTime(Byte.parseByte("0"));
        fundCollectionPlan.setGmtCreate(date);
        fundCollectionPlan.setGmtModified(date);
        return fundCollectionPlanDOMapper.insert(fundCollectionPlan) != null;
    }

    /**
     * @author: Redsheep
     * @Param planId
     * @return: boolean
     * @description: 手动终止归集计划
     * @data: 2019/8/16 15:56
     */
    public boolean shutdownFundCollectionPlan(Integer planId) {
        return fundCollectionPlanDOMapper.updateFundCollectionPlanStatus(planId, Byte.parseByte("101")) != null;
    }

    /**
     * @author: Redsheep
     * @Param planId
     * @return: boolean
     * @description: 自动取消归集计划
     * @data: 2019/8/16 15:57
     */
    public boolean cancelFundCollectionPlan(Integer planId) {
        return fundCollectionPlanDOMapper.updateFundCollectionPlanStatus(planId, Byte.parseByte("102")) != null;
    }

    /**
     * @author: Redsheep
     * @Param planId
     * @return: boolean
     * @description: 归集计划自动结束
     * @data: 2019/8/16 15:57
     */
    public boolean endFundCollectionPlan(Integer planId) {
        return fundCollectionPlanDOMapper.updateFundCollectionPlanStatus(planId, Byte.parseByte("103")) != null;
    }

    /**
     * @author: Redsheep
     * @param: void
     * @return: void
     * @description: 扫描当天的归集计划并执行
     * @data: 2019/8/19 14:07
     */
//    @Scheduled(cron = "0 0 0 1/1 * ? *")
    @Scheduled(cron = "0 0 0,18 * * ? ")
    public void execFundCollectionPlan() {
        // 查找当天的归集计划
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Integer month = c.get(Calendar.MONTH) + 1;
        Integer day = c.get(Calendar.DATE) + 1;
        List<FundCollectionPlan> fundCollectionPlans;
        fundCollectionPlans = fundCollectionPlanDOMapper.selectFundCollectionBySchedule(month, day);
        if (fundCollectionPlans == null) {
            return;
        }
        // 执行归集计划
        String uuid;
        ListIterator<FundCollectionPlan> fundCollectionPlanListIterator = fundCollectionPlans.listIterator();
        while (fundCollectionPlanListIterator.hasNext()) {
            fundCollectionPlan = fundCollectionPlanListIterator.next();
            transferRecord.setGmtCreate(date);
            transferRecord.setGmtModified(date);
            transferRecord.setTransferType(Byte.parseByte("107"));
            transferRecord.setBankInCard(bankCardService.selectBankCardNumberById(fundCollectionPlan.getBankInCardId()));
            transferRecord.setBankOutCard(bankCardService.selectBankCardNumberById(fundCollectionPlan.getBankOutCardId()));
            uuid = UUID.randomUUID().toString().replaceAll("-", "");
            transferRecord.setTransferRecordUuid(uuid);
            transferRecord.setTransferRecordAmount(fundCollectionPlan.getCollectionAmount());
            transferRecord.setTransferRecordTime(date);
            transferRecord.setUserId(fundCollectionPlan.getUserId());
            transferRecord.setTransferNote(fundCollectionPlan.getPlanName());
            transferRecord.setBankInCardName("本人");
            transferRecord.setBankInIdentification("");
            if (bankCardService.bankCardTransferBusines(fundCollectionPlan.getBankOutCardId(), fundCollectionPlan.getBankInCard(), fundCollectionPlan.getCollectionAmount())) {
                transferRecord.setTransferStatus(Byte.parseByte("101"));
                // 累加归集计划失败次数
                // 如果失败次数累计超过3次，自动取消归集计划
                if (fundCollectionPlan.getFailTime() >= 2) {
                    cancelFundCollectionPlan(fundCollectionPlan.getPlanId());
                }else{
                    fundCollectionPlanDOMapper.addFailTime(fundCollectionPlan.getPlanId());
                }
            } else {
                transferRecord.setTransferStatus(Byte.parseByte("102"));
                // 清空归集计划失败次数
                fundCollectionPlanDOMapper.resetFailTime(fundCollectionPlan.getPlanId());
            }
            // 写入转账记录表
            transferRecordService.insertTransferRecord(transferRecord);
            // 写入归集记录表
            fundCollectionRecordDO.setGmtCreate(date);
            fundCollectionRecordDO.setGmtModified(date);
            fundCollectionRecordDO.setRecordUuid(uuid);
            fundCollectionRecordDO.setPlanId(fundCollectionPlan.getPlanId());
            fundCollectionRecordDOMapper.insert(fundCollectionRecordDO);
            // 如果归集计划是一次性的，执行完就结束归集计划
            if (fundCollectionPlan.getCollectionMonth() != 0) {
                endFundCollectionPlan(fundCollectionPlan.getPlanId());
            }
        }
    }

    private String switchPlanStatus(String status) {
        switch (status) {
            case "100":
                return "进行中";
            case "101":
                return "已手动终止";
            case "102":
                return "已自动取消";
            case "103":
                return "已结束";
            default:
                return "未知";
        }
    }

}
