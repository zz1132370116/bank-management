package com.zl.dc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * @version: V1.0
 * @author: lu
 * @className: RedisInsertUtil
 * @description: redis工具类，
 * @data: 2019/8/19 10:47
 */

public class RedisInsertUtil {
    /**
     * @author: lu
     * @Param StringRedisTemplate stringRedisTemplate, String key, String value 为数字类型:
     * @return:
     * @description: 添加错误信息到redis，0点删除
     * @data: 2019/8/19 11:10
     */
    public static void addingData(StringRedisTemplate stringRedisTemplate, String key, String value) {
        stringRedisTemplate.afterPropertiesSet();
        Calendar calendar = Calendar.getInstance();
        Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
        Integer times = 0;

        if (value != null && !"".equals(value)) {
            times = Integer.parseInt(value);
        }
        times += 1;
        stringRedisTemplate.opsForValue().set(key, times.toString(), 24 - hour, TimeUnit.HOURS);
    }
}