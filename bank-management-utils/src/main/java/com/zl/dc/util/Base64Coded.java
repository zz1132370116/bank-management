package com.zl.dc.util;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
  * @version: V1.0
  * @author: zhanglei
  * @className: Base64Coded
  * @description: base64工具类
  * @data: 2019/8/10 13:42
  */
public class Base64Coded {
     public static void main(String[] args) {
         String string = "qianyang123";
         //编码
         String encode = encode(string.getBytes());
         System.out.println(string + "\t编码后的字符串为：" + encode);
         //解码
         String decode = decode(encode.getBytes());
         System.out.println(encode + "\t字符串解码后为：" + decode);
     }
     //base64 解码
     public static String decode(byte[] bytes) {
         return new String(Base64.decodeBase64(bytes));
     }

     //base64 编码
     public static String encode(byte[] bytes) {
         return new String(Base64.encodeBase64(bytes));
     }

    /**
     * @author pds
     * @param imageFile
     * @return java.lang.String
     * @description 将图片进行base64编码
     * @date 2019/8/14 19:57
     */
    public static String encodeImgageToBase64(File imageFile) {

        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        //其进行Base64编码处理
        byte[] data = null;
        //读取图片字节数组
        try {
            InputStream in = new FileInputStream(imageFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        //返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }
}
