package com.zl.dc.service;

import com.zl.dc.mapper.UserMapper;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.vo.BankUserVo;
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
        //拼接条件
        criteria.andEqualTo("idCard",idCard);
        //根据条件查询一个
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
        //拼接条件
        criteria.andEqualTo("userPhone",userPhone);
        //通过条件查询一个
        return userMapper.selectOneByExample(example);
    }

    /**
     * @author: pds
     * @params:  [user]
     * @return: com.zl.dc.pojo.BankUser
     * @description: 修改密码
     * @data: 2019/8/8 10:14
     */
    public BankUser updateBankUserPassword(BankUser user) {
        BankUser bankUserByUserPhone = this.getBankUserByUserPhone(user.getUserPhone());
        bankUserByUserPhone.setUserPassword(user.getUserPassword());
        userMapper.updateByPrimaryKeySelective(bankUserByUserPhone);
        return bankUserByUserPhone;
    }

    /**
     * @author: pds
     * @params:  [user]
     * @return: com.zl.dc.pojo.BankUser
     * @description: 修改手机
     * @data: 2019/8/10 10:21
     */
    public BankUser updateBankUserPhone(BankUserVo user){
        BankUser bankUserByUserPhone = this.getBankUserByUserPhone(user.getOldPhone());
        bankUserByUserPhone.setUserPhone(user.getUserPhone());
        userMapper.updateByPrimaryKeySelective(bankUserByUserPhone);
        return bankUserByUserPhone;
    }

    /**
     * @author pds
     * @param user
     * @return void
     * @description 功能描述
     * @date 2019/8/12 13:58
     */
    public void updateBankUserPhoneToEmpty(BankUser user){
        user.setUserPhone("");
        userMapper.updateByPrimaryKeySelective(user);
    }
}
