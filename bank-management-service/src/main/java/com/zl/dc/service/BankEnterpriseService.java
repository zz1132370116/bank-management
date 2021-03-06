package com.zl.dc.service;

import com.zl.dc.mapper.BankEnterpriseMapper;
import com.zl.dc.pojo.BankEnterprise;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @version V1.0
 * @author pds
 * @className BankEnterpriseService
 * @description 企业业务层
 * @date 2019/8/15 19:58
 */
@Service
@Transactional
public class BankEnterpriseService {
    @Resource
    private BankEnterpriseMapper bankEnterpriseMapper;

    /**
     * @author pds
     * @param enterpriseBankCard
     * @return com.zl.dc.pojo.BankEnterprise
     * @description 根据银行卡获取企业用户信息
     * @date 2019/8/16 10:18
     */
    public BankEnterprise getBankEnterpriseByEnterpriseBankCard(String enterpriseBankCard){
        Example example = new Example(BankEnterprise.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("enterpriseBankCard",enterpriseBankCard);
        BankEnterprise enterprise = bankEnterpriseMapper.selectOneByExample(example);
        return enterprise;
    }

    /**
     * @author pds
     * @param id
     * @return com.zl.dc.pojo.BankEnterprise
     * @description 根据id获取企业用户信息
     * @date 2019/8/16 14:50
     */
    public BankEnterprise getBankEnterpriseById(Integer id){
        BankEnterprise enterprise = bankEnterpriseMapper.selectByPrimaryKey(id);
        return enterprise;
    }
}
