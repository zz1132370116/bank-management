package com.zl.dc.util;

/**
 * @version: V1.0
 * @author: lu
 * @className: StringValid
 * @description: 字符串校验类
 * @data: 2019/8/13 11:47
 */
public class StringValid {

    /**
    * @author: lu
    * @param: string
    * @return:  boolean
    * @description: 功能描述
    * @data: 2019/8/13 11:53
    */
    public static boolean isBlank(String... str) {
        for (int i =0 ;i<str.length;i++){
            if ("".equals(str[i]) && str[i] == null) {
                return false;
            }
        }
        return true;
    }
}
