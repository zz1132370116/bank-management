package com.zl.dc.service;

import com.zl.dc.mapper.PayeeGroupMapper;
import com.zl.dc.pojo.BankManager;
import com.zl.dc.pojo.UserPayeeGroup;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PayeeGroupService {

    @Resource
    private PayeeGroupMapper payeeGroupMapper;

    /**
     * @author: lu
     * @Param Integer userId:
     * @return: List<UserPayeeGroup>
     * @description: 根据用户id返回用户所有的用户群组
     * @data: 2019/8/14 14:38
     */
    public List<UserPayeeGroup> selectPayeeGroupByUid(Integer userId) {
        Example example = new Example(UserPayeeGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        return payeeGroupMapper.selectByExample(example);
    }

}
