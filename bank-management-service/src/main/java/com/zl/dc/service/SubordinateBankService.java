package com.zl.dc.service;

import com.zl.dc.mapper.SubordinateBankMapper;
import com.zl.dc.pojo.SubordinateBank;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public List<SubordinateBank> getAllSubordinateBank() {
      return  subordinateBankMapper.selectAll();
    }

}
