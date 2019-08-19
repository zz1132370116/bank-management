package com.zl.dc.config;

import com.zl.dc.pojo.OtherBankCard;
import com.zl.dc.service.BankCardService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @author pds
 * @className BankCardAsync
 * @description 异步工具类
 * @date 2019/8/17 9:54
 */
@Component
public class BankCardAsync {
    @Resource
    private BankCardService bankCardService;

    /**
     * @author pds
     * @param otherBankCardList
     * @return void
     * @description 异步添加他行银行卡
     * @date 2019/8/17 9:58
     */
    @Async
    public void addOtherBankCardList(List<OtherBankCard> otherBankCardList){
        List<OtherBankCard> otherBankCards = bankCardService.getOtherBankCardByUserId(otherBankCardList.get(0).getUserId());
        Map<String, Integer> otherBankCardMap = otherBankCards.stream().collect(Collectors.toMap(OtherBankCard::getBankCardNumber, OtherBankCard::getUserId,
                (key1, key2) -> key2));
        for (OtherBankCard otherBankCard : otherBankCardList) {
            if (otherBankCardMap.containsKey(otherBankCard.getBankCardNumber())){
                break;
            }else {
                bankCardService.addOtherBankCard(otherBankCard);
            }
        }
    }
}
