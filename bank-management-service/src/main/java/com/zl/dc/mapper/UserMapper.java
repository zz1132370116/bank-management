package com.zl.dc.mapper;

import com.zl.dc.pojo.BankUser;
import tk.mybatis.mapper.common.Mapper;
/**
 * @version: V1.0
 * @author: zhanglei
 * @className: 用户Mapper
 * @description: TODO
 * @data: 2019/8/5 17:09
 */
@org.apache.ibatis.annotations.Mapper
public interface UserMapper extends Mapper<BankUser> {
}
