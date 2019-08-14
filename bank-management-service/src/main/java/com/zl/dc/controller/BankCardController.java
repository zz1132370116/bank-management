package com.zl.dc.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.zl.dc.config.SendUpgradeCard;
import com.zl.dc.pojo.BankCard;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.CrossBorderTransferRecord;
import com.zl.dc.pojo.OtherBankCard;
import com.zl.dc.service.BankCardService;
import com.zl.dc.vo.BaseResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @version: V1.0
 * @author: zhanglei
 * @className: BankCardController
 * @description: 银行卡控制层
 * @data: 2019/8/12 15:31
 */
@RequestMapping
@RestController
public class BankCardController {
    @Resource
    private BankCardService bankCardService;
    @Resource
    private StringRedisTemplate redisTemplate;
    /**
     * @author: zhanglei
     * @param: [bankUser]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 根据用户查询用户所持有的本行卡
     * @data: 2019/8/12 15:23
     */
    @PostMapping("/getBankCardByUser")
    public ResponseEntity<BaseResult> getBankCardByUser(@RequestBody BankUser bankUser) {
        if (bankUser.getUserId() != null) {
            List<BankCard> crossBorderTransferRecords = bankCardService.getBankCardByUser(bankUser);
            if (crossBorderTransferRecords != null) {
                return ResponseEntity.ok(new BaseResult(0, "查询成功").append("data", crossBorderTransferRecords));
            }
        }
        return ResponseEntity.ok(new BaseResult(1, "失败"));
    }

    /**
     * @author: Redsheep
     * @Param userId
     * @return: org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 根据用户id查询所有本行卡View信息
     * @data: 2019/8/14 9:04
     */
    @GetMapping("/getBankCardByUserId")
    public ResponseEntity<BaseResult> getBankCardByUserId(@RequestParam("userId") Integer userId) {
        if (userId == null) {
            return ResponseEntity.ok(new BaseResult(1, "参数错误"));
        }
        List<BankCard> bankCards = bankCardService.getBankCardByUserId(userId);
        if (bankCards != null) {
            return ResponseEntity.ok(new BaseResult(0, "查询成功").append("data", bankCards));
        }
        return ResponseEntity.ok(new BaseResult(1, "查询不到数据"));
    }

    @PostMapping("/addOtherBankCard")
    public ResponseEntity<BaseResult> addOtherBankCard(@RequestBody OtherBankCard otherBankCard) {
        return null;
    }
    /**
     * @author: zhanglei
     * @param: [bankCardId]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 根据银行卡ID查询银行卡信息
     * @data: 2019/8/14 11:32
     */
    @GetMapping("/getBankCardBybankCardId/{bankCardId}")
    public ResponseEntity<BaseResult> getBankCardBybankCardId(@PathVariable("bankCardId") String bankCardId){
        //非空非null判断
        if (StringUtils.isNotBlank(bankCardId)){
            //调用service
            BankCard bankCard = bankCardService.getBankCardBybankCardId(bankCardId);
            if (bankCard !=null){
                //返回
                return ResponseEntity.ok(new BaseResult(0,"查询成功").append("data",bankCard));
            }

        }
        return ResponseEntity.ok(new BaseResult(1,"查询失败"));
    }
    @GetMapping("/sendUpgradeCard/{bankCardId}")
    public ResponseEntity<BaseResult> sendUpgradeCard(@PathVariable("bankCardId") String bankCardId){
        //非空非null判断
        if (StringUtils.isNotBlank(bankCardId)){

            //调用service
            BankCard bankCard = bankCardService.getBankCardBybankCardId(bankCardId);
            if (bankCard !=null){
                //1 生产验证码
                String code = RandomStringUtils.randomNumeric(6);
                //发短信
                try {
                    SendSmsResponse sendSmsResponse = SendUpgradeCard.sendSms(bankCard.getBankCardPhone(), code);
                    //存redis5分钟，因为用户的信息是使用手机号为key存放到redis中的，为了防止将用户信息覆盖，
                    redisTemplate.opsForValue().set(bankCard.getBankCardPhone()+code, code, 5, TimeUnit.MINUTES);
                    if ("OK".equalsIgnoreCase(sendSmsResponse.getCode())) {
                        return ResponseEntity.ok(new BaseResult(0, "发送成功"));
                    }else {
                        return ResponseEntity.ok(new BaseResult(0, sendSmsResponse.getMessage()));
                    }
                } catch (ClientException e) {
                    e.printStackTrace();
                }
            }
            return ResponseEntity.ok(new BaseResult(1, "发送失败"));
        }
        return ResponseEntity.ok(new BaseResult(1, "发送失败"));
    }
}
