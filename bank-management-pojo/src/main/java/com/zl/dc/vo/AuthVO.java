package com.zl.dc.vo;

import com.sun.xml.internal.org.jvnet.fastinfoset.sax.FastInfosetReader;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @version: V1.0
 * @author: redsheep
 * @className: AuthVO
 * @description: 验证用户、付款银行卡、支付密码的VO
 * @data: 2019/8/16 15:29
 */
@Data
@AllArgsConstructor
public class AuthVO {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 付款银行卡id
     */
    private Integer bankCardId;

    /**
     * 付款密码
     */
    private String password;
    /**
     * 数据
     */
    private Object data;
}
