package com.zl.dc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zl.dc.api.VerifyIdCard;
import com.zl.dc.mapper.UserMapper;
import com.zl.dc.pojo.BankCard;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.OtherBankCard;
import com.zl.dc.util.MD5;
import com.zl.dc.vo.BankUserVo;
import com.zl.dc.vo.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
     @Resource
     private StringRedisTemplate redisTemplate;
     @Autowired
     private BankCardService bankCardService;

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
     * @description 将用户的手机号置为null
     * @date 2019/8/12 13:58
     */
    public void updateBankUserPhoneToNull(BankUser user){
        user.setUserPhone(null);
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * @author pds
     * @param bankUser
     * @return java.lang.Integer
     * @description 添加一个用户
     * @date 2019/8/14 11:18
     */
    public Integer addBankUser(BankUser bankUser) {
        int insert = userMapper.insert(bankUser);
        return insert;
    }
    /**
    * @author: lu
    * @Param Integer uid:
    * @return: BankUser
    * @description: 根据用户ID查询用户
    * @data: 2019/8/13 20:40
    */
    public BankUser selectBankUserByUid(Integer uid){
        return userMapper.selectByPrimaryKey(uid);
    }


    /**
     * @author pds
     * @param file
     * @param userId
     * @return java.lang.Integer
     * @description 用户进行实名认证
     * @date 2019/8/15 11:41
     */
    public Integer verifiedIdentity(List<MultipartFile> file,Integer userId) throws IOException, ParseException {
        MultipartFile frontMul = file.get(0);
        MultipartFile backMul = file.get(1);
        // 获取文件名
        String frontFileName = frontMul.getOriginalFilename();
        String backFileName = backMul.getOriginalFilename();
        // 获取文件后缀
        String frontPrefix=frontFileName.substring(frontFileName.lastIndexOf("."));
        String backPrefix=backFileName.substring(backFileName.lastIndexOf("."));
        //将MultipartFile转为File
        File frontFile = File.createTempFile(UUID.randomUUID().toString().replaceAll("-", ""), frontPrefix);
        frontMul.transferTo(frontFile);
        File backFile = File.createTempFile(UUID.randomUUID().toString().replaceAll("-", ""), backPrefix);
        backMul.transferTo(backFile);
        //校验身份证背面，并判断身份证是否过期
        String backJson = VerifyIdCard.back(backFile);
        JSONObject backJsonObject = JSONObject.parseObject(backJson);
        String expirationDateStr = (String) backJsonObject.getJSONObject("words_result").getJSONObject("失效日期").get("words");
        String expiration = expirationDateStr.substring(0,4)+"-"+expirationDateStr.substring(4,6)+"-"+expirationDateStr.substring(6);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date expirationDate = simpleDateFormat.parse(expiration);
        Integer integer = VerifyIdCard.compareDate(expirationDate, new Date());
        if (integer == -1){
            return -2;
        }
        //校验身份证正面，并获取用户姓名、身份证号码
        String frontJson = VerifyIdCard.front(frontFile);
        JSONObject frontJsonObject = JSONObject.parseObject(frontJson);
        JSONObject wordsResult = frontJsonObject.getJSONObject("words_result");
        String userName = (String) wordsResult.getJSONObject("姓名").get("words");
        String idCard = (String) wordsResult.getJSONObject("公民身份号码").get("words");

        BankUser user = new BankUser();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setIdCard(idCard);
        Integer i = userMapper.updateByPrimaryKeySelective(user);

        user = userMapper.selectByPrimaryKey(userId);
        System.out.println(user);
        redisTemplate.opsForValue().set(user.getUserId().toString(), JSON.toJSONString(user));

        return i;
    }

    /**
     * @author pds
     * @param bankUserVo
     * @return java.lang.Boolean
     * @description 设置默认银行卡
     * @date 2019/8/20 9:13
     */
    public Boolean setDefaultBankCard(BankUserVo bankUserVo) {
        BankCard bankCard = bankCardService.selectBankCardByid(bankUserVo.getBankCardId());
        if (bankCard != null){
            String password = MD5.GetMD5Code(bankUserVo.getPassword());
            if (password.equals(bankCard.getBankCardPassword())){
                BankUser user = new BankUser();
                user.setUserId(bankUserVo.getUserId());
                user.setDefaultBankCard(bankCard.getBankCardNumber());
                userMapper.updateByPrimaryKeySelective(user);
                return true;
            }
        }
        return false;
    }
}
