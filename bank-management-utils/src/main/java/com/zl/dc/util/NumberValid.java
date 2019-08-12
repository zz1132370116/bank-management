package com.zl.dc.util;


import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pds
 * @version V1.0
 * @className Redsheep
 * @description 金额校验工具类
 * @date 2019/8/11 19:57
 */
public class NumberValid {

    /**
     * @author pds
     * @Param money
     * @return: boolean
     * @description 判断金额字符串是否符合规范，小数点前11位，小数点后2位
     * @date 2019/8/11 20:14
     */
    public static boolean moneyValid(String money) {

        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d{10})|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match = pattern.matcher(money);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }
}
