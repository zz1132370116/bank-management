package com.zl.dc.util;

/**
 * @version: V1.0
 * @author: redsheep
 * @className: StarUtil
 * @description: 身份证、手机号、银行卡号加**工具类
 * @data: 2019/8/13 17:50
 */
public class StarUtil {

    /**
     * @author: Redsheep
     * @Param str 需要加*的字符串
     * @Param front 前保留位数
     * @Param end 后保留位数
     * @return: java.lang.String
     * @description:
     * @data: 2019/8/13 17:59
     */
    public static String StringAddStar(String str, int front, int end) {
        // 判空
        if (str == null || "".equals(str)) {

            return null;
        }
        //需要截取的长度不能大于身份证号长度
        if ((front + end) > str.length()) {
            return null;
        }
        //需要截取的不能小于0
        if (front < 0 || end < 0) {
            return null;
        }
        // 计算*数量
        int asteriskCount = str.length() - (front + end);
        StringBuffer asteriskStr = new StringBuffer();
        for (int i = 0; i < asteriskCount; i++) {
            asteriskStr.append("*");
        }
        // 利用正则替换
        String regex = "(\\w{" + String.valueOf(front) + "})(\\w+)(\\w{" + String.valueOf(end) + "})";
        return str.replaceAll(regex, "$1" + asteriskStr + "$3");
    }

}
