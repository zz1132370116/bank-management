package com.zl.dc.service;

import com.zl.dc.mapper.CrossBorderTransferRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
  * @version: V1.0
  * @author: zhanglei
  * @className: CrossBorderTransferRecordService
  * @description: 跨境转账操作层
  * @data: 2019/8/10 16:44
  */
 @Service
 @Transactional
public class CrossBorderTransferRecordService {
     @Resource
    private CrossBorderTransferRecordMapper crossBorderTransferRecordMapper;
}
