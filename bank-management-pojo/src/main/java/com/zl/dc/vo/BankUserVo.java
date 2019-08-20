package com.zl.dc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.util.Date;

/**
 * @version V1.0
 * @author pds
 * @className BankUserVo
 * @description BankUser的vo
 * @date 2019/8/11 20:14
 */
@Data
public class BankUserVo {
    private Integer userId;
    private String userName;
    private String userPhone;
    private String userPassword;
    private Byte userStatus;
    private String defaultBankCard;
    private String idCard;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModified;
    /**
     * 接收验证码
     */
    private String code;
    /**
     * 修改手机功能的原手机号
     */
    private String oldPhone;
    /**
     * 修改密码功能的确认密码
     */
    private String passwordConfig;
    private Integer bankCardId;
    private String password;
}
