package com.zl.dc.api;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Auther: 舌頭會游泳
 * @Date: 2019/8/3 14:43
 * @Description: 获取银行卡标识
 */
public class AccessBank {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        String str = getCardDetail("6221505915000450309");
        JSONObject json = JSONObject.parseObject(str);
        System.out.println(getCardDetail("6221505915000450309"));
        System.out.println(json.toJSONString());
        System.out.println(json.get("bank"));
        System.out.println(json.get("validated"));
    }

    /**
     * @param cardNo 银行卡卡号
     * @return {"bank":"CMB","validated":true,"cardType":"DC","key":"(卡号)","messages":[],"stat":"ok"}
     * 2017年5月22日 下午4:35:23
     */
    public static String getCardDetail(String cardNo) {
        // 创建HttpClient实例
        String url = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardNo=";
        url += cardNo;
        url += "&cardBinCheck=true";
        StringBuilder sb = new StringBuilder();
        try {
            URL urlObject = new URL(url);
            URLConnection uc = urlObject.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getSubordinateBank(String cardNo) {
        String cardDetail = getCardDetail(cardNo);
        JSONObject json = JSONObject.parseObject(cardDetail);
        if (json.get("bank") == null) {
            return null;
        }
        return json.get("bank").toString();
    }

}
