package com.zl.dc.controller;

import com.alibaba.fastjson.JSON;
import com.zl.dc.pojo.BankCard;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.ManagerTranscation;
import com.zl.dc.service.ActiveGatheringService;
import com.zl.dc.vo.ActiveGatheringVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    private StringRedisTemplate redisTemplate;
    @Resource
    HttpSession session;
    /**
     * @author: nwm
     * @param: * getActiveCollection
     * @return: * List<ActiveGatheringVo>
     * @description: 用户进入主动转账页面时执行的方法
     * @data: 2019/8/13 19:00
     */
    @GetMapping("/getActiveCollection/{userId}")
    //ResponseEntity<BaseResult>
     public List<ActiveGatheringVo> getActiveCollection(@PathVariable("userId") Integer userId){
        String userStr = redisTemplate.opsForValue().get(userId.toString());
        BankUser user = (BankUser) JSON.parse(userStr);

        return ags.getActiveGatheringVoList(userId);
     }

    /**
     * @author: nwm
     * @param: * updateGatheringStatus
     * @return: * boolean
     * @description: 用户修改主动收款状态为取消时执行的方法,付款用户拒绝付款时执行的方法
     * @data: 2019/8/13 19:00
     */
    @PostMapping("/updateGatheringType/{activeId}")
    public boolean  updateGatheringStatus(@PathVariable("activeId") String activeId){
        //修改该订单id的收款状态为取消
        return ags.updateGatheringStatus(activeId);
    }
    /**
     * @author: nwm
     * @param: * addTransactionTecord
     * @return: * HashMap<String,Object>
     * @description: 用户添加主动收款时执行的方法
     * @data: 2019/8/13 19:00
     */
    @PostMapping("/addTransactionTecord")
    public boolean addTransactionTecord(@RequestBody ActiveGatheringVo agvo){
        //根据发起主动收款用户填写的数据添加交易订单
        //agvo 收款订单基本信息
        //userPhone 付款人电话
        //userBankId 收款卡id
        return ags.addTransactionTecord(agvo);
    }
    //
    /**
     * @author: nwm
     * @param: * TransferRecord
     * @return: * HashMap<String,Object>
     * @description: 用户进入消息中心页面时执行的方法
     * @data: 2019/8/13 19:00
     */
    @GetMapping("/getMessageCenter/{userId}/{userName}")
    public Map<String,Object> getMessageCenter(@PathVariable("userId") Integer userId,@PathVariable("userName") String userName){
        //查询相关收款记录
        List<ActiveGatheringVo> agVOList=ags.getActiveGatheringVo(userName);
        //查询相关提额记录
         List<ManagerTranscation> mtList=ags.getManagerTranscation(userId);
        Map<String,Object> map=new HashMap<>();
        map.put("activeGatheringVo",agVOList);
        map.put("managerTranscation",mtList);
        return map;
    }

    /**
     * @author: nwm
     * @param: * agreeGathering
     * @return: * HashMap<String,Object>
     * @description: 用户同意付款时执行的方法
     * @data: 2019/8/13 19:00
     */
    @PostMapping("/agreeGathering")
    public  boolean agreeGathering(@RequestBody ActiveGatheringVo agvo){

        return ags.agreeGathering(agvo);
    }

    /**
     * @author: nwm
     * @param: * updateManagerTranscationStatus
     * @return: * HashMap<String,Object>
     * @description: 用户主动取消升级卡申请方法
     * @data: 2019/8/13 19:00
     */
    @PostMapping("/updateManagerTranscationStatus/{transcationId}")
    public  boolean updateManagerTranscationStatus(@PathVariable("transcationId") String transcationId){
        //根据事务表id修改订单为取消
        return  ags.updateManagerTranscationStatus(transcationId);
    }

    /**
     * @author: nwm
     * @param: * getBankCardByUser
     * @return: * List<BankCard>
     * @description: 根据登录的用户查询用户名下的银行卡
     * @data: 2019/8/13 19:00
     */
    @GetMapping("/getBankCardByUser/{userId}")
    public   List<BankCard>  getBankCardByUser(@PathVariable("userId") Integer userId){
        //获取当前登录用户id
        return  ags.getBankCardByUser(userId);
    }
    //




}
