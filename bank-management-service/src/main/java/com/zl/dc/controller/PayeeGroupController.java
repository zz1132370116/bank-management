package com.zl.dc.controller;

import com.zl.dc.pojo.UserPayee;
import com.zl.dc.pojo.UserPayeeGroup;
import com.zl.dc.service.PayeeGroupService;
import com.zl.dc.service.UserPayeeService;
import com.zl.dc.vo.BaseResult;
import com.zl.dc.vo.NewPayeeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @version: V1.0
 * @author: lu
 * @className: PayeeGroupController
 * @description: 收款群组控制层
 * @data: 2019/8/14 14:15
 */
@RestController
public class PayeeGroupController {
    @Autowired
    private PayeeGroupService payeeGroupService;
    @Autowired
    private UserPayeeService UserPayeeService;

    /**
     * @author: lu
     * @Param Integer userId:
     * @return: ResponseEntity<BaseResult>
     * @description: 根据用户id返回用户所有的用户群组
     * @data: 2019/8/14 14:42
     */
    @PostMapping("/selectPayeeGroupByUid")
    public ResponseEntity<BaseResult> selectPayeeGroupByUid(@RequestBody Integer userId) {
        if (userId == null) {
            return ResponseEntity.ok(new BaseResult(1, "异常状态请登录后操作"));
        }
        List<UserPayeeGroup> userPayeeGroups = payeeGroupService.selectPayeeGroupByUid(userId);
        return ResponseEntity.ok(new BaseResult(0, "成功").append("data", userPayeeGroups));
    }

    /**
     * @author: lu
     * @Param Integer userId:
     * @return: ResponseEntity<BaseResult>
     * @description: 根据群组id返回所有的收款人
     * @data: 2019/8/14 14:42
     */
    @PostMapping("/selectPayeeById")
    public ResponseEntity<BaseResult> selectPayeeById(@RequestBody Integer payeeGroupId) {
        if (payeeGroupId == null) {
            return ResponseEntity.ok(new BaseResult(1, "异常状态请联系管理员"));
        }
        List<UserPayee> userPayees = UserPayeeService.selectPayeeById(payeeGroupId);
        return ResponseEntity.ok(new BaseResult(0, "成功").append("data", userPayees));
    }

    /**
     * @author: lu
     * @Param NewPayeeVo:
     * @return: ResponseEntity<BaseResult>
     * @description: 添加一条收款群组
     * @data: 2019/8/14 15:24
     */
    public ResponseEntity<BaseResult> addPayeeGroup(@RequestBody String newPayeeVo) {
        if (newPayeeVo == null) {
            return ResponseEntity.ok(new BaseResult(1, "异常状态请联系管理员"));
        }
        return null;
    }
}
