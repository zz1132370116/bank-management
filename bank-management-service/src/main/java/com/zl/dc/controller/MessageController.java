package com.zl.dc.controller;

import com.zl.dc.pojo.ManagerTranscation;
import com.zl.dc.service.MessageService;
import com.zl.dc.vo.BaseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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

    @GetMapping("/MessageNotification/{userId}")
     public ResponseEntity<BaseResult> MessageNotification(@PathVariable("userId")String userId){
        if (StringUtils.isNotBlank(userId)){
            List<ManagerTranscation> managerTranscations = messageService.MessageNotification(userId);
            if (managerTranscations !=null){
                return ResponseEntity.ok(new BaseResult(0,"消息查询成功").append("data",managerTranscations));
            }else{
                return ResponseEntity.ok(new BaseResult(1,"当前用户没有消息"));
            }

        }
            return ResponseEntity.ok(new BaseResult(2,"消息查询失败"));


    }
}
