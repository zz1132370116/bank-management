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


    public BankEnterprise getBankEnterpriseByEnterpriseBankCard(String enterpriseBankCard){
        Example example = new Example(BankEnterprise.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("enterpriseBankCard",enterpriseBankCard);
        BankEnterprise enterprise = bankEnterpriseMapper.selectOneByExample(example);
        return enterprise;
    }

}
