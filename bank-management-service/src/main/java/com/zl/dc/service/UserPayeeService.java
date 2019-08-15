package com.zl.dc.service;

import com.zl.dc.mapper.PayeeGroupMapper;
import com.zl.dc.mapper.PayeeMapper;
import com.zl.dc.pojo.UserPayee;
import com.zl.dc.pojo.UserPayeeGroup;
import com.zl.dc.vo.NewPayeeGroupVo;
import com.zl.dc.vo.NewPayeeVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class UserPayeeService {

    @Resource
    private PayeeMapper payeeMapper;
    @Resource
    private PayeeGroupMapper payeeGroupMapper;
    @Resource
    private UserPayeeGroup userPayeeGroup;
    @Resource
    private UserPayee userPayee;

    /**
     * @author: lu
     * @Param Integer userId:
     * @return: List<UserPayeeGroup>
     * @description: 根据用户id返回用户所有的用户群组
     * @data: 2019/8/14 14:38
     */
    public List<UserPayee> selectPayeeById(Integer payeeGroupId) {
        Example example = new Example(UserPayee.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("payeeGroupId", payeeGroupId);
        return payeeMapper.selectByExample(example);
    }

    /**
     * @author: lu
     * @Param NewPayeeGroupVo:
     * @return: boolean
     * @description: 根据页面提交Vo添加新群组
     * @data: 2019/8/14 16:03
     */
    public boolean addPayeeGroup(NewPayeeGroupVo newPayeeGroupVo) {
        userPayeeGroup.setPayeeGroupName(newPayeeGroupVo.getInputPayeeGroup());
        userPayeeGroup.setUserId(newPayeeGroupVo.getUserId());
        userPayeeGroup.setIsAbandoned(Byte.parseByte("0"));
        userPayeeGroup.setGmtCreate(new Date());
        userPayeeGroup.setGmtModified(new Date());
        int insert = payeeGroupMapper.insert(userPayeeGroup);
        if (insert == -1) {
            return false;
        }
        return true;
    }

    /**
     * @author: lu
     * @Param Integer payeeId:
     * @return: boolean
     * @description: 根据id删除群组用户
     * @data: 2019/8/14 16:40
     */
    public boolean deletePayeeById(Integer payeeId) {
        int delete = payeeMapper.deleteByPrimaryKey(payeeId);
        if (delete == -1) {
            return false;
        }
        return true;
    }

    /**
     * @author: lu
     * @Param NewPayeeVo:
     * @return: UserPayee
     * @description: 添加一条收款人
     * @data: 2019/8/14 19:18
     */
    public UserPayee addPayee(NewPayeeVo newPayeeVo) {
        userPayee.setGmtCreate(new Date());
        userPayee.setGmtModified(new Date());
        userPayee.setPayeeBankCard(newPayeeVo.getPayeeBankCard());
        userPayee.setPayeeBankIdentification(newPayeeVo.getPayeeBankIdentification());
        userPayee.setPayeeGroupId(newPayeeVo.getPayeeGroupId());
        userPayee.setPayeeName(newPayeeVo.getPayeeName());

        int insertId = payeeMapper.insert(userPayee);
        if(insertId <0){
            return  null ;
        }
        return payeeMapper.selectByPrimaryKey(insertId);
    }
}
