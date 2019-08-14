package com.zl.dc.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/** 例如：
 *  BaseResult baseResult = new BaseResult("1","提示失败");
 *  baseResult.append("token","......");
 * Created by liangtong.
 */
@Data
public class BaseResult {
    //存放数据
    private Map<String,Object> data = new HashMap<>();

    /**
     * 2个必填项
     * @param errno  提示码,0表示成功，1表示失败
     * @param errmsg  错误提示信息
     */
    public BaseResult(Integer errno, String errmsg) {
        data.put("errno" , errno);
        data.put("errmsg" , errmsg);
    }


    public BaseResult append(String key , Object msg){
        data.put(key , msg);
        return this;
    }
}
