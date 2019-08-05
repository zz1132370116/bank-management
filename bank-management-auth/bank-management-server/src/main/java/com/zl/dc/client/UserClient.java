package com.zl.dc.client;

import com.zl.dc.entity.UserEntity;
import com.zl.dc.pojo.BankUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author 舍頭襘游泳
 * @date 2018/12/13
 */

@FeignClient("web-service")
public interface UserClient {
    @GetMapping("query")
    ResponseEntity<BankUser> queryUser(@RequestParam("userPhone") String mobile,
                                       @RequestParam("userPassword") String password,@RequestParam("idCard") String idCard);
}