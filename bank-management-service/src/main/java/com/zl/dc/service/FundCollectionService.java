package com.zl.dc.service;

import com.zl.dc.mapper.FundCollectionPlanDOMapper;
import com.zl.dc.pojo.FundCollectionPlan;
import com.zl.dc.pojo.TransferRecord;
import com.zl.dc.util.StarUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

@Service
@Transactional
public class FundCollectionService {
    @Resource
    FundCollectionPlanDOMapper fundCollectionPlanDOMapper;
    @Resource
    TransferRecord transferRecord;
    @Resource
    BankCardService bankCardService;

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
