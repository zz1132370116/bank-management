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
     * @param errno  提示码
     * @param errmsg  错误提示信息
     */
    public BaseResult(Integer errno, String errmsg) {
        data.put("errno" , errno);
        data.put("errmsg" , errmsg);
    }

    /**
     * 追加其他参数，支持链式
     * @param key
     * @param msg
     * @return
     */
    public BaseResult append(String key , Object msg){
        data.put(key , msg);
        return this;
    }
}
