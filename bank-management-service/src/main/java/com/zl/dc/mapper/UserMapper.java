package com.zl.dc.mapper;

import com.zl.dc.pojo.BankUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @version: V1.0
 * @author: zhanglei
 * @className: 用户Mapper
 * @description: TODO
 * @data: 2019/8/5 17:09
 */
@org.apache.ibatis.annotations.Mapper
public interface UserMapper extends Mapper<BankUser> {
    /**
     * @author: zhanglei
     * @param: [userName, idCard]
     * @return:java.util.List<com.zl.dc.pojo.BankUser>
     * @description: 条件查询所有用户
     * @data: 2019/8/7 14:39
     */
    @Select("SELECT * FROM bank_user WHERE user_name = #{user_name} and id_card = #{id_card} and user_id > (#{pageNum}-1)*10 LIMIT 10; ")
    List<BankUser> getUserListByParams(@Param("user_name")String userName,@Param("id_card")String idCard,@Param("pageNum")Integer pageNum);
    /**
     * @author: zhanglei
     * @param: [userName, idCard]
     * @return:java.util.List<com.zl.dc.pojo.BankUser>
     * @description: 根据用户名分页查询用户
     * @data: 2019/8/7 14:39
     */
    @Select("SELECT user_id,user_name,user_phone,user_password,user_status,default_bank_card,id_card,gmt_create,gmt_modified FROM bank_user WHERE user_name = #{user_name} and user_id > (#{pageNum}-1)*10 LIMIT 10;")
    List<BankUser> getUserListByUserName(@Param("user_name") String userName,@Param("pageNum") Integer pageNum);
    /**
     * @author: zhanglei
     * @param: [userName, idCard]
     * @return:java.util.List<com.zl.dc.pojo.BankUser>
     * @description: 根据银行卡号分页查询用户
     * @data: 2019/8/7 14:39
     */
    @Select("SELECT user_id,user_name,user_phone,user_password,user_status,default_bank_card,id_card,gmt_create,gmt_modified FROM bank_user WHERE id_card = #{id_card} and user_id > (#{pageNum}-1)*10 LIMIT 10;")
    List<BankUser> getUserListByIDCARD(@Param("id_card")String idCard, @Param("pageNum")Integer pageNum);
    /**
     * @author: zhanglei
     * @param: []
     * @return:java.util.List<com.zl.dc.pojo.BankUser>
     * @description: 查询会员数
     * @data: 2019/8/6 14:04
     */
    @Select("user_id,user_name,user_phone,user_password,user_status,default_bank_card,id_card,gmt_create,gmt_modified FROM bank_user where user_id > (#{pageNum}-1)*10 LIMIT 10; ")
    List<BankUser> GetUserList(@Param("pageNum") Integer pageNum);
}

