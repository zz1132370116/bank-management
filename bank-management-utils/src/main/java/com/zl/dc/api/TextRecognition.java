package com.zl.dc.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @version: V1.0
 * @author: pds
 * @className: TextRecognition
 * @description: 百度云身份证识别类
 * @data: 2019/8/8 20:37
 */
public class TextRecognition {
    /**
     * @author: pds
     * @params:  [httpUrl, httpArg]
     * @return: java.lang.String
     * @description: 百度云身份证识别
     * @data: 2019/8/8 20:37
     */
    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        try {
            //用java JDK自带的URL去请求
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();

            //设置该请求的消息头
            //设置HTTP方法：POST
            connection.setRequestMethod("POST");
            //设置其Header的Content-Type参数为application/x-www-form-urlencoded
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            //填入apikey到HTTP header
            connection.setRequestProperty("apikey", "aCKNUQCANw0ov7xqZo3P0iVr");
            //将第二步获取到的token填入到HTTP header
            connection.setRequestProperty("access_token", AuthService.getAuth());
            connection.setDoOutput(true);
            connection.getOutputStream().write(httpArg.getBytes("UTF-8"));
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
