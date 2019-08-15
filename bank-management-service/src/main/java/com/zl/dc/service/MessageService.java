package com.zl.dc.service;

import com.zl.dc.mapper.ManagerTranscationMapper;
import com.zl.dc.pojo.ManagerTranscation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class MessageService {
    @Resource
    private ManagerTranscationMapper managerTranscationMapper;
    /**
     * @author: zhanglei
     * @param: [userId]
     * @return:java.util.List<com.zl.dc.pojo.ManagerTranscation>
     * @description: 消息通知
     * @data: 2019/8/15 20:09
     */
    public List<ManagerTranscation> MessageNotification(String userId) {
        Example example = new Example(ManagerTranscation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",Integer.parseInt(userId));
        criteria.andNotEqualTo("gmtModified",null);
        criteria.andNotEqualTo("transcationStatus",0);
        List<ManagerTranscation> managerTranscations = managerTranscationMapper.selectByExample(example);
        return managerTranscations;
    }
}
