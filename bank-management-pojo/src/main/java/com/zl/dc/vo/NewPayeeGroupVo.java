package com.zl.dc.vo;

import lombok.Data;
import lombok.ToString;

/**
* @version: V1.0
* @author: lu
* @className: NewPayeeGroupVo
* @description: 收款群组VO
* @data: 2019/8/14 15:20
*/
@Data
@ToString
public class NewPayeeGroupVo {
    /**
     * 收款人id
     */
    private Integer userId;
    /**
     * 群组名字
     */
    private String inputPayeeGroup;

}
