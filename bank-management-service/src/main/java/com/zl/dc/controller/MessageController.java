package com.zl.dc.controller;

import com.alibaba.fastjson.JSON;
import com.zl.dc.pojo.ManagerTranscation;
import com.zl.dc.service.MessageService;
import com.zl.dc.vo.BaseResult;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
  * @version: V1.0
  * @author: zhanglei
  * @className: MessageController
  * @description: 消息通知
  * @data: 2019/8/15 20:06
  */
@RestController
@RequestMapping
public class MessageController {
    @Resource
    private MessageService messageService;
    @Resource
    private StringRedisTemplate redisTemplate;
    /**
     * @author: zhanglei
     * @param: [userId]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 查询用户的通知
     * @data: 2019/8/16 14:19
     */
    @GetMapping("/MessageNotification/{userId}")
     public ResponseEntity<BaseResult> MessageNotification(@PathVariable("userId")String userId){

        if (StringUtils.isNotBlank(userId)){

            List<ManagerTranscation> managerTranscations = messageService.MessageNotification(userId);
            redisTemplate.opsForValue().set(userId+"message", managerTranscations.toString(),5,TimeUnit.DAYS);

            if (managerTranscations !=null){
                for (ManagerTranscation managerTranscation : managerTranscations) {
                    managerTranscation.setTranscationStatus(Byte.parseByte("100"));
                    messageService.updateStatus(managerTranscation);
                }
                return ResponseEntity.ok(new BaseResult(0,"消息查询成功").append("data",managerTranscations));
            }else{
                return ResponseEntity.ok(new BaseResult(1,"当前用户没有消息"));
            }

        }
            return ResponseEntity.ok(new BaseResult(2,"消息查询失败"));

    }

}
