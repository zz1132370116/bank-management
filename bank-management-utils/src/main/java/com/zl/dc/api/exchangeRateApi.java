package com.zl.dc.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zl.dc.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
  * @version: V1.0
  * @author: zhanglei
  * @className: exchangeRateApi
  * @description: 汇率工具类
  * @data: 2019/8/10 10:05
  */
public class exchangeRateApi {
    /**
     * @author: zhanglei
     * @param: [amount, from, to]
     * @return:void
     * @description: 金额
     * @data: 2019/8/10 10:27
     */
     public static String exChangeRatePrice(String  amount, String from, String to){

         String host = "https://jisuhuilv.market.alicloudapi.com";
         String path = "/exchange/convert";
         String method = "GET";
         String appcode = "f138853b46b2475185f098ada5b04760";
         Map<String, String> headers = new HashMap<String, String>();
         //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
         headers.put("Authorization", "APPCODE " + appcode);
         Map<String, String> querys = new HashMap<String, String>();
         querys.put("amount", amount);
         querys.put("from", from);
         querys.put("to", to);


         try {
             /**
              * 重要提示如下:
              * HttpUtils请从
              * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
              * 下载
              *
              * 相应的依赖请参照
              * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
              */
             HttpResponse httpResponse = HttpUtils.doGet(host, path, method, headers, querys);
             String ent =EntityUtils.toString(httpResponse.getEntity());
             JSONObject jsonObject = JSON.parseObject(ent);
             JSONObject  result = (JSONObject)jsonObject.get("result");
            // System.out.println(  result.get("camount"));
             //System.out.println(EntityUtils.toString(httpResponse.getEntity()));

             return  result.get("camount").toString();
             //获取response的body
         } catch (Exception e) {
             e.printStackTrace();
         }
         return null;
     }

    /**
     * @author: zhanglei
     * @param: [amount, from, to]
     * @return:void
     * @description: 汇率
     * @data: 2019/8/10 10:27
     */
    public static String exChangeRate(String amount,String from,String to){

        String host = "https://jisuhuilv.market.alicloudapi.com";
        String path = "/exchange/convert";
        String method = "GET";
        String appcode = "f138853b46b2475185f098ada5b04760";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("amount", amount);
        querys.put("from", from);
        querys.put("to", to);


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse httpResponse = HttpUtils.doGet(host, path, method, headers, querys);
            String ent =EntityUtils.toString(httpResponse.getEntity());
            JSONObject jsonObject = JSON.parseObject(ent);
            JSONObject  result = (JSONObject)jsonObject.get("result");
            //System.out.println(  result.get("rate"));
            //System.out.println(EntityUtils.toString(httpResponse.getEntity()));

            return  result.get("rate").toString();
            //获取response的body
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
