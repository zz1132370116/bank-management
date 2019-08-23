package com.zl.dc.controller;

import com.zl.dc.pojo.UserPayee;
import com.zl.dc.pojo.UserPayeeGroup;
import com.zl.dc.service.PayeeGroupService;
import com.zl.dc.service.SubordinateBankService;
import com.zl.dc.service.UserPayeeService;
import com.zl.dc.vo.BaseResult;
import com.zl.dc.vo.NewPayeeGroupVo;
import com.zl.dc.vo.NewPayeeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @Autowired
    private SubordinateBankService subordinateBankService;


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
        for (UserPayee userPayee : userPayees) {
            //查询所属银行进行回显
            String bankCarkForName = subordinateBankService.selectBankNameByBankCardIdentification(userPayee.getPayeeBankIdentification());
            userPayee.setBankCardName(bankCarkForName);
        }
        return ResponseEntity.ok(new BaseResult(0, "成功").append("data", userPayees));
    }

    /**
     * @author: lu
     * @Param NewPayeeVo:
     * @return: ResponseEntity<BaseResult>
     * @description: 添加一条收款群组
     * @data: 2019/8/14 15:24
     */
    @RequestMapping("/addPayeeGroup")
    public ResponseEntity<BaseResult> addPayeeGroup(@RequestBody NewPayeeGroupVo newPayeeGroupVo) {
        if (newPayeeGroupVo == null) {
            return ResponseEntity.ok(new BaseResult(1, "异常状态请联系管理员"));
        }
        if (StringUtils.isBlank(newPayeeGroupVo.getInputPayeeGroup())) {
            return ResponseEntity.ok(new BaseResult(1, "请正确输入新建新建群组名称"));
        }
        if (newPayeeGroupVo.getUserId() == null) {
            return ResponseEntity.ok(new BaseResult(1, "异常状态请重新登录"));
        }
        boolean status = UserPayeeService.addPayeeGroup(newPayeeGroupVo);
        if (status == false) {
            return ResponseEntity.ok(new BaseResult(1, "异常状态请联系管理员"));
        }
        return ResponseEntity.ok(new BaseResult(0, "成功"));
    }

    /**
     * @author: lu
     * @Param Integer payeeId:
     * @return: ResponseEntity<BaseResult>
     * @description: 根据id删除群组用户
     * @data: 2019/8/14 16:37
     */
    @RequestMapping("/deletePayeeById")
    public ResponseEntity<BaseResult> deletePayeeById(@RequestBody Integer payeeId) {
        if (payeeId == null) {
            return ResponseEntity.ok(new BaseResult(1, "异常状态请联系管理员"));
        }
        boolean status = UserPayeeService.deletePayeeById(payeeId);
        if (status) {
            return ResponseEntity.ok(new BaseResult(0, "删除成功"));
        } else {
            return ResponseEntity.ok(new BaseResult(1, "异常状态请联系管理员"));
        }
    }

    /**
     * @author: lu
     * @Param NewPayeeVo:
     * @return: ResponseEntity<BaseResult>
     * @description: 根据VO添加群组收款人
     * @data: 2019/8/14 19:28
     */
    @RequestMapping("/addPayee")
    public ResponseEntity<BaseResult> addPayee(@RequestBody NewPayeeVo newPayeeVo) {
        if (newPayeeVo == null) {
            return ResponseEntity.ok(new BaseResult(1, "异常状态请联系管理员"));
        }
        if (StringUtils.isBlank(newPayeeVo.getPayeeName())) {
            return ResponseEntity.ok(new BaseResult(1, "异常状态请联系管理员"));
        }
        if (StringUtils.isBlank(newPayeeVo.getPayeeBankCard())) {
            return ResponseEntity.ok(new BaseResult(1, "异常状态请联系管理员"));
        }
        if (StringUtils.isBlank(newPayeeVo.getPayeeBankIdentification())) {
            return ResponseEntity.ok(new BaseResult(1, "异常状态请联系管理员"));
        }
        if (newPayeeVo.getPayeeGroupId() == null) {
            return ResponseEntity.ok(new BaseResult(1, "异常状态请联系管理员"));
        }
        UserPayee userPayee = UserPayeeService.addPayee(newPayeeVo);
        if (userPayee == null) {
            return ResponseEntity.ok(new BaseResult(1, "添加失败请重试"));
        }
        return ResponseEntity.ok(new BaseResult(0, "成功").append("data", userPayee));
    }

}
