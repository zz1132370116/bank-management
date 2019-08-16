package com.zl.dc.controller;


import com.zl.dc.pojo.ManagerTranscation;
import com.zl.dc.service.ActiveGatheringService;
import com.zl.dc.util.NumberValid;
import com.zl.dc.vo.ActiveGatheringVo;
import com.zl.dc.vo.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * @version: V1.0
 * @author: nwm
 * @className: java.util.List<com.zl.dc.pojo.CrossBorderTransferRecord
 * @description: 主动收款相关控制层
 * @data: 2019/8/13
 */
@RestController
@RequestMapping
public class ActiveGatheringController {
    @Autowired
    ActiveGatheringService ags;
    @Resource
    private StringRedisTemplate redisTemplate;
    @Resource
    HttpSession session;
    /**
     * @author: nwm
     * @param: * getActiveCollection
     * @return: * java.util.List<com.zl.dc.pojo.CrossBorderTransferRecord>
     * @description: 用户进入主动转账页面时执行的方法
     * @data: 2019/8/13 19:00
     */
    @GetMapping("/getActiveCollection/{userId}")
    //ResponseEntity<BaseResult>
     public ResponseEntity<BaseResult> getActiveCollection(@PathVariable("userId") String userId){
        if (!NumberValid.primaryKey(userId)){
            return ResponseEntity.ok(new BaseResult(1, "失败"));
        }
        return ResponseEntity.ok(new BaseResult(0, "查询成功").append("data", ags.getActiveGatheringVoList(new Integer(userId))));
       /* 后台从redis中获取登录的用户信息
       String userStr = redisTemplate.opsForValue().get(userId.toString());
        BankUser user = (BankUser) JSON.parse(userStr);*/
     }

    /**
     * @author: nwm
     * @param: * updateGatheringStatus
     * @return: * java.util.List<com.zl.dc.pojo.CrossBorderTransferRecord>
     * @description: 用户修改主动收款状态为取消时执行的方法,付款用户拒绝付款时执行的方法
     * @data: 2019/8/13 19:00
     */
    @PostMapping("/updateGatheringType/{activeId}")
    public ResponseEntity<BaseResult>  updateGatheringStatus(@PathVariable("activeId") String activeId){
        if (!NumberValid.primaryKey(activeId)){
            boolean flag=ags.updateGatheringStatus(activeId);
            if (flag){
                return ResponseEntity.ok(new BaseResult(0, "操作成功"));
            }
        }
        return ResponseEntity.ok(new BaseResult(1, "失败"));
    }
    /**
     * @author: nwm
     * @param: * addTransactionTecord
     * @return: * java.util.List<com.zl.dc.pojo.CrossBorderTransferRecord>
     * @description: 用户添加主动收款时执行的方法
     * @data: 2019/8/13 19:00
     */
    @PostMapping("/addTransactionTecord")
    public ResponseEntity<BaseResult> addTransactionTecord(@RequestBody ActiveGatheringVo agvo){
        //根据发起主动收款用户填写的数据添加交易订单
        //agvo 收款订单基本信息
        //userPhone 付款人电话
        //userBankId 收款卡id
        if( NumberValid.verifyPhone(agvo.getOutUserPhone())){
            return ResponseEntity.ok(new BaseResult(1, "失败"));
        }
        if(agvo.getInBankId()==null){
            return ResponseEntity.ok(new BaseResult(1, "失败"));
        }
        if(NumberValid.moneyValid(agvo.getMuchMoney().toString())){
            return ResponseEntity.ok(new BaseResult(1, "失败"));
        }
        if(agvo.getTransferRemarks()==null || "".equals(agvo.getTransferRemarks())){
            return ResponseEntity.ok(new BaseResult(1, "失败"));
        }
        return ResponseEntity.ok(new BaseResult(0, "查询成功").append("data", ags.addTransactionTecord(agvo)));
    }
    //
    /**
     * @author: nwm
     * @param: * TransferRecord
     * @return: * java.util.List<com.zl.dc.pojo.CrossBorderTransferRecord>
     * @description: 用户进入消息中心页面时执行的方法
     * @data: 2019/8/13 19:00
     */
    @GetMapping("/getMessageCenter/{userId}/{userName}")
    public ResponseEntity<BaseResult> getMessageCenter(@PathVariable("userId") String userId,@PathVariable("userName") String userName){
        if (!NumberValid.primaryKey(userId)){
            return ResponseEntity.ok(new BaseResult(1, "失败"));
        }
        if (userName==null){
            return ResponseEntity.ok(new BaseResult(1, "失败"));
        }
        //查询相关待付款记录
        List<ActiveGatheringVo> agVOList=ags.getActiveGatheringVo(userName);
        //查询相关提额记录
         List<ManagerTranscation> mtList=ags.getManagerTranscation(new Integer(userId));
        return ResponseEntity.ok(new BaseResult(0, "查询成功").append("data", agVOList).append("data",mtList));
    }

    /**
     * @author: nwm
     * @param: * agreeGathering
     * @return: * java.util.List<com.zl.dc.pojo.CrossBorderTransferRecord>
     * @description: 用户同意付款时执行的方法
     * @data: 2019/8/13 19:00
     */
    @PostMapping("/agreeGathering")
    public  ResponseEntity<BaseResult> agreeGathering(@RequestBody ActiveGatheringVo agvo){
        if(agvo!=null){
            boolean flag=ags.agreeGathering(agvo);
            if (flag){
                 return ResponseEntity.ok(new BaseResult(0, "操作成功"));
            }
        }
        return ResponseEntity.ok(new BaseResult(1, "失败"));
    }

    /**
     * @author: nwm
     * @param: * updateManagerTranscationStatus
     * @return: * java.util.List<com.zl.dc.pojo.CrossBorderTransferRecord>
     * @description: 用户主动取消升级卡申请方法
     * @data: 2019/8/13 19:00
     */
    @PostMapping("/updateManagerTranscationStatus/{transcationId}")
    public ResponseEntity<BaseResult> updateManagerTranscationStatus(@PathVariable("transcationId") String transcationId){
        if (!NumberValid.primaryKey(transcationId)){
            //根据事务表id修改订单为取消
            boolean flag=ags.updateManagerTranscationStatus(transcationId);
            if (flag){
                return ResponseEntity.ok(new BaseResult(0, "操作成功"));
            }
        }
        return ResponseEntity.ok(new BaseResult(1, "失败"));
    }
    /**
     * @author: nwm
     * @param: * getBankCardByUser
     * @return: * java.util.List<com.zl.dc.pojo.CrossBorderTransferRecord>
     * @description: 根据登录的用户查询用户名下的银行卡
     * @data: 2019/8/13 19:00
     */
    @GetMapping("/getBankCardByUser/{userId}")
    public   ResponseEntity<BaseResult>  getBankCardByUser(@PathVariable("userId") String userId){
        if (!NumberValid.primaryKey(userId)){
            return ResponseEntity.ok(new BaseResult(1, "失败"));
        }
        //获取当前登录用户id
        return ResponseEntity.ok(new BaseResult(0, "查询成功").append("data",ags.getBankCardByUser(new Integer(userId))));
    }
}
