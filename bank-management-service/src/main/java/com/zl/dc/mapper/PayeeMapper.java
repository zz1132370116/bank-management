package com.zl.dc.mapper;

import com.zl.dc.pojo.UserPayee;
import com.zl.dc.pojo.UserPayeeGroup;
import tk.mybatis.mapper.common.Mapper;

/**
 * @version: V1.0
 * @author: lu
 * @className: PayeeMapper
 * @description: 收款人
 * @data: 2019/8/14
 */
@org.apache.ibatis.annotations.Mapper
public interface PayeeMapper extends Mapper<UserPayee> {
}
