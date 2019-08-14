package com.zl.dc.client;


import com.zl.dc.pojo.BankUser;
import com.zl.dc.vo.BankUserVo;
import com.zl.dc.vo.BaseResult;
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
     * @param user
     * @return org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description 功能描述
     * @date 2019/8/12 16:29
     */
    @PostMapping("query")
    BaseResult queryUser(@RequestBody BankUser user);


    /**
     * @author pds
     * @param user
     * @return com.zl.dc.vo.BaseResult
     * @description 功能描述
     * @date 2019/8/13 9:01
     */
    @PostMapping("loginBySendSms")
    BaseResult loginBySendSms(@RequestBody BankUserVo user);

}