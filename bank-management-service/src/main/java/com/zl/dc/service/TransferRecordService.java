package com.zl.dc.service;

import com.alibaba.fastjson.JSONObject;
import com.zl.dc.mapper.SubordinateBankMapper;
import com.zl.dc.mapper.TransferRecordMapper;
import com.zl.dc.mapper.UserMapper;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.SubordinateBank;
import com.zl.dc.pojo.TransferRecord;
import com.zl.dc.util.AccessBank;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @version: V1.0
 * @author: zhanglei
 * @className: TransferRecordService
 * @description: 转账记录操作层
 * @data: 2019/8/6 19:15
 */
@Service
@Transactional
public class TransferRecordService {
    @Resource
    private TransferRecordMapper transferRecordMapper;
    @Resource
    private UserMapper bankUserMapper;
    @Resource
    private SubordinateBankMapper subordinateBankMapper;




}
