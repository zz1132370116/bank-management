package com.zl.dc.client;


import com.zl.dc.pojo.BankUser;
import com.zl.dc.vo.BankUserVo;
import com.zl.dc.vo.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @version V1.0
 * @author pds
 * @className UserClient
 * @description 用户接口
 * @date 2019/8/15 17:33
 */
@FeignClient("web-service")
public interface UserClient {

    /**
     * @author pds
     * @param bankUserVo
     * @return com.zl.dc.vo.BaseResult
     * @description 注册----注册
     * @date 2019/8/14 9:17
     */
    @PostMapping("/register")
    BaseResult register(@RequestBody BankUserVo bankUserVo);

    /**
     * @author pds
     * @param user
     * @return org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description 登录----使用密码登录
     * @date 2019/8/12 16:29
     */
    @PostMapping("query")
    BaseResult queryUser(@RequestBody BankUser user);


    /**
     * @author pds
     * @param user
     * @return com.zl.dc.vo.BaseResult
     * @description 登录----使用验证码登录
     * @date 2019/8/13 9:01
     */
    @PostMapping("loginBySendSms")
    BaseResult loginBySendSms(@RequestBody BankUserVo user);

    /**
     * @author pds
     * @param userId
     * @return com.zl.dc.vo.BaseResult
     * @description 退出登录
     * @date 2019/8/16 10:26
     */
    @GetMapping("/signOut")
    BaseResult signOut(@RequestParam("userId") Integer userId);

}