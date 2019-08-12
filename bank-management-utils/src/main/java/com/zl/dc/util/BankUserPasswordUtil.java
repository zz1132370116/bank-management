package com.zl.dc.util;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.util.Random;

/**
 * @version V1.0
 * @author pds
 * @className BankUserPasswordUtil
 * @description MD5加盐加密
 * @date 2019/8/11 21:02
 */
public class BankUserPasswordUtil {

    /**
     * @author pds
     * @param password
     * @return java.lang.String
     * @description 功能描述
     * @date 2019/8/11 21:03
     */
    public static String generate(String password) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        String salt = sb.toString();
        password = md5Hex(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }
    /**
     * @author pds
     * @param password
     * @param md5
     * @return boolean
     * @description 校验密码是否正确
     * @date 2019/8/11 21:03
     */
    public static boolean verify(String password, String md5) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }
        String salt = new String(cs2);
        return md5Hex(password + salt).equals(new String(cs1));
    }
    /**
     * @author pds
     * @param src
     * @return java.lang.String
     * @description 获取十六进制字符串形式的MD5摘要
     * @date 2019/8/11 21:02
     */
    public static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        // 加密+加盐
        String password1 = generate("12345");
        System.out.println("结果：" + password1 + "   长度："+ password1.length());
        // 解码
        System.out.println(verify("12345", password1));
        // 加密+加盐
        String password2= generate("admin");
        System.out.println("结果：" + password2 + "   长度："+ password2.length());
        // 解码
        System.out.println(verify("admin", password2));
    }

}
