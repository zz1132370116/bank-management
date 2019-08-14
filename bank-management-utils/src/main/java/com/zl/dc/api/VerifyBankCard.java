package com.zl.dc.api;

import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.internal.function.json.Append;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.prefs.AbstractPreferences;

/**
 * @version: V1.0
 * @author: redsheep
 * @className: BankCardValid
 * @description: 银行卡二元素、三元素校验
 * @data: 2019/8/14 14:11
 */
public class VerifyBankCard {

    private static String appkey = "bingmeiyouqian";

    /**
     * @author: Redsheep
     * @Param bankCard
     * @Param name
     * @return: java.lang.String
     * @description: 银行卡号、姓名校验
     * @data: 2019/8/14 14:38
     */
    public static String verifyBankCard2(String bankCard, String name) {

        StringBuilder url = new StringBuilder("http://v.juhe.cn/verifybankcard/query");
        StringBuilder res = new StringBuilder();
        String uelName = null;
        try {
            uelName = URLEncoder.encode(name, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url.append("?key=").append(appkey).append("&")
                .append("realname=")
                .append(uelName)
                .append("&")
                .append("bankcard=")
                .append(bankCard);
        try {
            URL urlObject = new URL(url.toString());
            URLConnection uc = urlObject.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String input;
            while ((input = in.readLine()) != null) {
                res.append(input);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    /**
     * @author: Redsheep
     * @Param bankCard
     * @Param name
     * @Param idCard
     * @return: java.lang.String
     * @description: 银行卡号、姓名、身份证号校验
     * @data: 2019/8/14 14:45
     */
    public static String verifyBankCard3(String bankCard, String name, String idCard) {
        StringBuilder url = new StringBuilder("http://v.juhe.cn/verifybankcard3/query");
        StringBuilder res = new StringBuilder();
        String uelName = null;
        try {
            uelName = URLEncoder.encode(name, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url.append("?key=").append(appkey).append("&")
                .append("realname=")
                .append(uelName)
                .append("&")
                .append("bankcard=")
                .append(bankCard)
                .append("&")
                .append("idcard=")
                .append(idCard);

        try {
            URL urlObject = new URL(url.toString());
            URLConnection uc = urlObject.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String input;
            while ((input = in.readLine()) != null) {
                res.append(input);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    /**
     * @author: Redsheep
     * @Param apiResult
     * @return: java.lang.String
     * @description: 获取api调用的error_code
     * @data: 2019/8/14 14:50
     */
    public static String getErrorCode(String apiResult){
        JSONObject json = JSONObject.parseObject(apiResult);
        return json.get("error_code").toString();
    }
}
