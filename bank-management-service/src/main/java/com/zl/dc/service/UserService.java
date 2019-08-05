package com.zl.dc.service;

import com.zl.dc.mapper.UserMapper;
import com.zl.dc.pojo.BankUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
  * @version: V1.0
  * @author: zhanglei
  * @className: 用户Service
  * @description: TODO
  * @data: 2019/8/5 17:08
  */
 @Service
 @Transactional
public class UserService {
     @Resource
    private UserMapper userMapper;
    /**
     * @author: zhanglei
     * @param: [idCard]
     * @return:com.zl.dc.pojo.BankUser
     * @description: 通过身份证号查询用户信息
     * @data: 2019/8/5 17:10
     */
    public BankUser getBankUserByIdCard(String idCard) {
      //创建条件
        Example example = new Example(BankUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("idCard",idCard);
        return userMapper.selectOneByExample(example);
    }
    /**
     * @author: zhanglei
     * @param: [userPhone]
     * @return:com.zl.dc.pojo.BankUser
     * @description: 通过手机号查询用户信息
     * @data: 2019/8/5 17:11
     */
    public BankUser getBankUserByUserPhone(String userPhone) {
        //创建条件
        Example example = new Example(BankUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userPhone",userPhone);
        return userMapper.selectOneByExample(example);
    }
}
