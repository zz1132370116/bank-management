package com.zl.dc.controller;

import com.alibaba.fastjson.JSON;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.service.UserService;
import com.zl.dc.vo.BaseResult;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
  * @version: V1.0
  * @author: zhanglei
  * @className: 用户Controller
  * @description: TODO
  * @data: 2019/8/5 17:04
  */
 @RequestMapping
 @RestController
public class UserController {
    @Resource
    private UserService userService;
     @Resource
     private StringRedisTemplate redisTemplate;

     /**
      * @author: zhanglei
      * @param: [bankUser]
      * @return:org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
      * @description: 功能描述
      * @data: 2019/8/5 17:05
      */
     @PostMapping("/query")
     public ResponseEntity<BaseResult> queryUser(@RequestBody BankUser bankUser) {
         BankUser user=null;
         //1 通过手机号查询用户
         if (bankUser.getUserPhone().equals("")){
             //通过身份证号查询用户信息
              user = this.userService.getBankUserByIdCard(bankUser.getIdCard());
         }else{
             //通过手机号查询用户信息
           user = this.userService.getBankUserByUserPhone(bankUser.getUserPhone());
         }
         //非空判断
         if (user !=null){
             //校验密码
             if (user.getUserPassword().equals(bankUser.getUserPassword())){
                 //将用户信息存redis,user对象转为字符串
                 redisTemplate.opsForValue().set(user.getUserPhone(), JSON.toJSONString(user));
                 return ResponseEntity.ok(new BaseResult(0,"登录成功").append("data",user));
             }
         }
         //3 正确
         return ResponseEntity.ok(new BaseResult(1, "登录失败"));
     }
}
