package com.zl.dc.client;


import com.zl.dc.pojo.BankUser;
import com.zl.dc.vo.BankUserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author 舍頭襘游泳
 * @date 2018/12/13
 */

@FeignClient("web-service")
public interface UserClient {

    /**
    * @author pds
    * @param  user
    * @return com.zl.dc.pojo.BankUser
    * @description 根据手机号或身份证号登录
    * @date 2019/8/11 15:53
    */
    @PostMapping("query")
    BankUser queryUser(@RequestBody BankUser user);

    /**
    * @author pds
    * @param user
    * @return com.zl.dc.vo.BankUserVo
    * @description 使用手机验证码登录
    * @date 2019/8/11 15:53
    */
    @PostMapping("loginBySendSms")
    BankUser loginBySendSms(@RequestBody BankUserVo user);
}