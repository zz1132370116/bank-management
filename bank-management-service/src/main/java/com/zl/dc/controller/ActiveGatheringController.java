package com.zl.dc.controller;

import com.zl.dc.pojo.BankCard;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.ManagerTranscation;
import com.zl.dc.service.ActiveGatheringService;
import com.zl.dc.service.BankCardService;
import com.zl.dc.vo.ActiveGatheringVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

/**
 * @version: V1.0
 * @author: nwm
 * @className: ActiveGatheringController
 * @description: 主动收款相关控制层
 * @data: 2019/8/13
 */
@RestController
@RequestMapping
public class ActiveGatheringController {
    @Autowired
    ActiveGatheringService ags;
    @Autowired
    BankCardService bcs;
    @Autowired
    HttpSession session;
    /**
     * @author: nwm
     * @param: * getActiveCollection
     * @return: * List<ActiveGatheringVo>
     * @description: 用户进入主动转账页面时执行的方法
     * @data: 2019/8/13 19:00
     */
    @GetMapping("/getActiveCollection")
     public List<ActiveGatheringVo> getActiveCollection(){
         return null;
     }

    /**
     * @author: nwm
     * @param: * updateGatheringStatus
     * @return: * HashMap<String,Object>
     * @description: 用户修改主动收款状态是执行的方法
     * @data: 2019/8/13 19:00
     */
    @PostMapping("/updateGatheringType/{id}")
    public HashMap<String,Object>  updateGatheringStatus(@PathVariable("id") String activeId){
        //修改该订单id的收款状态为取消
        return null;
    }
    /**
     * @author: nwm
     * @param: * addTransactionTecord
     * @return: * HashMap<String,Object>
     * @description: 用户添加主动收款时执行的方法
     * @data: 2019/8/13 19:00
     */
    @PostMapping("/addTransactionTecord/{userPhone}/{userBankId}")
    public HashMap<String,Object> addTransactionTecord(@PathVariable("userPhone") String userPhone,@PathVariable("userBankId") String userBankId ,ActiveGatheringVo agvo){
        //根据发起主动收款用户填写的数据添加交易订单
        //agvo 收款订单基本信息
        //userPhone 付款人电话
        //userBankId 收款卡id
        return null;
    }
    //
    /**
     * @author: nwm
     * @param: * TransferRecord
     * @return: * HashMap<String,Object>
     * @description: 用户进入消息中心页面时执行的方法
     * @data: 2019/8/13 19:00
     */
    @GetMapping("/getMessageCenter")
    public HashMap<String,Object> getMessageCenter(){
        //查询相关收款记录
        List<ActiveGatheringVo> agVOList=null;
        //查询相关提额记录
        List<ManagerTranscation> mtList=null;
        return null;
    }

    /**
     * @author: nwm
     * @param: * cancelGathering
     * @return: * HashMap<String,Object>
     * @description: 用户拒绝付款时执行的方法
     * @data: 2019/8/13 19:00
     */
    @PostMapping("/cancelGathering/{id}")
    public  HashMap<String,Object> cancelGathering(@PathVariable("id") String activeId){
        //根据订单id修改主动收款状态为取消
        return null;
    }

    /**
     * @author: nwm
     * @param: * agreeGathering
     * @return: * HashMap<String,Object>
     * @description: 用户同意付款时执行的方法
     * @data: 2019/8/13 19:00
     */
    @PostMapping("/agreeGathering/{transferRecordId}/{bankCardId}/{bankCardPassword}")
    public  HashMap<String,Object> agreeGathering(
            @PathVariable("transferRecordId") String transferRecordId,@PathVariable("bankCardId") String bankCardId,@PathVariable("bankCardPassword") String bankCardPassword){
        //根据用户传的订单id, transferRecordId
        // 银行卡id bankCardId
        // 银行卡密码.bankCardPassword
        //执行转账
        return null;
    }

    /**
     * @author: nwm
     * @param: * updateManagerTranscationStatus
     * @return: * HashMap<String,Object>
     * @description: 用户拒绝付款时执行的方法
     * @data: 2019/8/13 19:00
     */
    @PostMapping("/updateManagerTranscationStatus/{id}")
    public  HashMap<String,Object> updateManagerTranscationStatus(@PathVariable("id") String transcationId){
        //根据事务表id修改订单为取消
        return  null;
    }

    /**
     * @author: nwm
     * @param: * getBankCardByUser
     * @return: * List<BankCard>
     * @description: 根据登录的用户查询用户名下的银行卡
     * @data: 2019/8/13 19:00
     */
    @GetMapping("/getBankCardByUser")
    public   List<BankCard>  getBankCardByUser(){
        BankUser bankUser=new BankUser();
        //session中获取当前登录用户id
        return  bcs.getBankCardByUser(bankUser);
    }
    //




}
