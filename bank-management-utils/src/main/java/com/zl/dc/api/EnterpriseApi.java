package com.zl.dc.api;

import com.zl.dc.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
  * @version: V1.0
  * @author: zhanglei
  * @className: EnterpriseApi
  * @description: 企业工商信息工具类
  * @data: 2019/8/10 10:32
  */
public class EnterpriseApi {

    public static void main(String[] args) {
        String host = "https://jisuqygsxx.market.alicloudapi.com";
        String path = "/enterprise/query";
        String method = "GET";
        String appcode = "f138853b46b2475185f098ada5b04760";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("company", "杭州网尚科技有限公司");
//        querys.put("creditno", "creditno");
//        querys.put("regno", "regno");


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
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            //System.out.println(response.toString());
            //获取response的body
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @author: zhanglei
     * @param: [company, creditno, regno]
     * @return:void
     * @description: 查询企业工商信息
     * @data: 2019/8/10 10:34
     */
    public static void enterprise(String company,String creditno,String regno){
        String host = "https://jisuqygsxx.market.alicloudapi.com";
        String path = "/enterprise/query";
        String method = "GET";
        String appcode = "f138853b46b2475185f098ada5b04760";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("company", company);
        querys.put("creditno", creditno);
        querys.put("regno", regno);


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
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            //System.out.println(response.toString());
            //获取response的body
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
