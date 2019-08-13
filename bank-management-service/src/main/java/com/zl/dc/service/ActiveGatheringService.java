package com.zl.dc.service;

import com.zl.dc.vo.ActiveGatheringVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @version: V1.0
 * @author: nwm
 * @className: ActiveGatheringService
 * @description: 主动收款相关业务层
 * @data: 2019/8/13
 */
@Service
@Transactional
public class ActiveGatheringService {

    @Autowired
    private HttpSession session;

    /**
     * @author: nwm
     * @param: * getActiveGatheringVoList
     * @return: * boolean
     * @description: 查询所有主动收款记录
     * @data: 2019/8/13 19:00
     */
    public List<ActiveGatheringVo> getActiveGatheringVoList(){
        return null;
    }
}
