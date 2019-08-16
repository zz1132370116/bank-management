package com.zl.dc.util;


import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pds
 * @version V1.0
 * @className Redsheep
 * @description 数值校验工具类
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

        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d{0,10})|([0]{1}))(\\.(\\d){1,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match = pattern.matcher(money);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param phone
     * @return java.lang.Boolean
     * @author pds
     * @description 将验证手机号是否正确
     * @date 2019/8/11 17:30
     */
    public static Boolean verifyPhone(String phone) {
        //验证手机号是否正确
        String regex = "^[1][3,4,5,7,8][0-9]{9}$";
        if (!phone.matches(regex)) {
            return false;
        }
        return true;
    }

    /**
     * @param idCard
     * @return java.lang.Boolean
     * @author pds
     * @description 将验证身份证号是否正确
     * @date 2019/8/11 17:30
     */
    public static Boolean verifyIdCard(String idCard) {
        //验证手机号是否正确
        String regex = "(^\\d{18}$)|(^\\d{15}$)";
        if (!idCard.matches(regex)) {
            return false;
        }
        return true;
    }

    /**
     * @param id
     * @return java.lang.Boolean
     * @author nwm
     * @description 校验主键是否是纯数字
     * @date 2019/8/16 10:30
     */
    public static Boolean primaryKey(String id) {
        String reg = "^\\d+$";
        if (!id.matches(reg)) {
            return false;
        }
        return true;
    }

}
