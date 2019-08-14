package com.zl.dc.vo;

import lombok.*;
import org.springframework.stereotype.Component;

/**
 * @version: V1.0
 * @author: redsheep
 * @className: PageBean
 * @description: 分页封装
 * @data: 2019/8/13 11:12
 */
@Data
@Component
public class PageBean {

    /**
     * 当前页数，从1开始
     */
    private int page;
    private int pageSize;
    private int index;
}
