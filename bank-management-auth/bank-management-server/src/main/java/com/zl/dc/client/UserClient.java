package com.zl.dc.client;


import com.zl.dc.pojo.BankUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author 舍頭襘游泳
 * @date 2018/12/13
 */

@FeignClient("web-service")
public interface UserClient {
    /**
     * @author: zhanglei
     * @param: [mobile, password, idCard]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.pojo.BankUser>
     * @description: 根据手机号或身份证号登录
     * @data: 2019/8/5 19:17
     */
    @PostMapping("query")
    ResponseEntity<BankUser> queryUser(@RequestParam("userPhone") String mobile,
                                       @RequestParam("userPassword") String password, @RequestParam("idCard") String idCard);

    /**
     * @author: zhanglei
     * @param: [userPhone, userPassword]
     * @return:org.springframework.http.ResponseEntity<com.zl.dc.pojo.BankUser>
     * @description: 根据验证码进行登录
     * @data: 2019/8/5 19:16
     */
    @PostMapping("loginBySendSms")
    ResponseEntity<BankUser> loginBySendSms(@RequestParam("userPhone") String userPhone, @RequestParam("userPassword") String userPassword);
}