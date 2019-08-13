package com.zl.dc.controller;

import com.zl.dc.pojo.BankCard;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.CrossBorderTransferRecord;
import com.zl.dc.service.BankCardService;
import com.zl.dc.vo.BaseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}