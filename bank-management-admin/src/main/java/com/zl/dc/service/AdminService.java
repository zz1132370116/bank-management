package com.zl.dc.service;

import com.zl.dc.client.AdminClient;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
  * @version: V1.0
  * @author: zhanglei
  * @className: AdminService
  * @description: 管理员操作层
  * @data: 2019/8/6 9:09
  */
 @Service
public class AdminService {
     @Resource
    private AdminClient adminClient;

}
