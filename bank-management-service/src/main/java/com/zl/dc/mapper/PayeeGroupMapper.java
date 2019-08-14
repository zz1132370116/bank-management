package com.zl.dc.mapper;

import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.UserPayeeGroup;
import tk.mybatis.mapper.common.Mapper;

/**
 * @version: V1.0
 * @author: lu
 * @className: PayeeGroupMapper
 * @description: 收款群组
 * @data: 2019/8/14
 */
@org.apache.ibatis.annotations.Mapper
public interface PayeeGroupMapper extends Mapper<UserPayeeGroup> {
}
