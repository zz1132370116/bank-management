package com.zl.dc.api;

import com.zl.dc.util.Base64Coded;

import java.io.File;

/**
 * @version V1.0
 * @author pds
 * @className VerifyIdCard
 * @description 身份证实名认证类
 * @date 2019/8/14 19:54
 */
public class VerifyIdCard {

    /**
     * @author pds
     * @param file
     * @return java.lang.String
     * @description 身份证正面
     * @date 2019/8/14 19:58
     */
    public static String front(File file){
        String jsonResult = verify(file,"front");
        return jsonResult;
    }

    /**
     * @author pds
     * @param file
     * @return java.lang.String
     * @description 身份证背面
     * @date 2019/8/14 19:59
     */
    public static String back(File file){
        String jsonResult = verify(file,"back");
        return jsonResult;
    }

    private static String verify(File file,String type){
        //进行BASE64位编码
        String imageBase = Base64Coded.encodeImgageToBase64(file);
        imageBase = imageBase.replaceAll("\r\n", "");
        imageBase = imageBase.replaceAll("\\+", "%2B");
        //百度云的文字识别接口,后面参数为获取到的token
        String httpUrl = " https://aip.baidubce.com/rest/2.0/ocr/v1/idcard?access_token="+AuthService.getAuth();
        String httpArg = "detect_direction=true&id_card_side="+type+"&image=" + imageBase;
        String jsonResult = TextRecognition.request(httpUrl, httpArg);
        return jsonResult;
    }
}
