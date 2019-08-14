package com.zl.dc.controller;

import com.zl.dc.pojo.BankCard;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.CrossBorderTransferRecord;
import com.zl.dc.service.BankCardService;
import com.zl.dc.vo.BaseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
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

    /**
     * @author: zhanglei
     * @param: [bankUser]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 根据用户查询用户所持有的本行卡
     * @data: 2019/8/12 15:23
     */
    @PostMapping("/getBankCardByUser")
    public ResponseEntity<BaseResult> getBankCardByUser(@RequestBody BankUser bankUser) {
        if (bankUser.getUserId() != null ){
            List<BankCard> crossBorderTransferRecords =bankCardService.getBankCardByUser(bankUser);
            if (crossBorderTransferRecords != null){
                return ResponseEntity.ok(new BaseResult(0,"查询成功").append("data",crossBorderTransferRecords));
            }
        }
        return ResponseEntity.ok(new BaseResult(1, "失败"));
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
}
