package com.zl.dc.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
  * @version: V1.0
  * @author: zhanglei
  * @className: AdminClient
  * @description: 远程调用
  * @data: 2019/8/6 9:11
  */
 @FeignClient("web-service")
public interface AdminClient {

}
