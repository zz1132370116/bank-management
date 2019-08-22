package com.zl.dc.service;

import com.zl.dc.mapper.SubordinateBankMapper;
import com.zl.dc.pojo.SubordinateBank;
import com.zl.dc.pojo.UserPayeeGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version: V1.0
 * @author: lu
 * @className: 所属银行业务层
 * @description:
 * @data: 2019/8/11 16:16
 */
@Service
public class SubordinateBankService {

    //注入所属银行mapper
    @Resource
    private SubordinateBankMapper subordinateBankMapper;

    /**
     * @author: lu
     * @Param 【】
     * @return: List<SubordinateBank>
     * @description: 查询所有的合作银行
     * @data: 2019/8/22 15:03
     */
    public List<SubordinateBank> getAllSubordinateBank() {
        return subordinateBankMapper.selectAll();
    }


    /**
     * @author: lu
     * @Param null:
     * @return: null
     * @description: 根据银行卡标识返回银行名字
     * @data: 2019/8/22 15:05
     */
    public String selectBankNameByBankCardIdentification(String identification) {
        Example example = new Example(SubordinateBank.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("bankIdentification", identification);
        SubordinateBank subordinateBank = subordinateBankMapper.selectOneByExample(example);
        return subordinateBank.getBankName();
    }

}
