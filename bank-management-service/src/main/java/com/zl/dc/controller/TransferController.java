package com.zl.dc.controller;

import com.alibaba.fastjson.JSON;
import com.zl.dc.pojo.*;
import com.zl.dc.service.BankCardService;
import com.zl.dc.service.SubordinateBankService;
import com.zl.dc.service.TransferRecordService;
import com.zl.dc.service.UserService;
import com.zl.dc.util.NumberValid;
import com.zl.dc.vo.BaseResult;
import com.zl.dc.vo.TransferValueVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @version: V1.0
 * @author: lu
 * @className: 单次转账控制层
 * @description:
 * @data: 2019/8/11 16:16
 */
@RestController
public class TransferController {

    @Autowired
    private SubordinateBankService subordinateBankService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private BankCardService bankCardService;

    @Autowired
    private TransferRecordService transferRecordService;

    @Autowired
    private UserService userService;

    /**
     * @author: lu
     * @param: []
     * @return: org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description: 用于查询出所有所属银行的方法
     * @data: 2019/8/11 17:38
     */
    @GetMapping("getAllSubordinateBank")
    public ResponseEntity<BaseResult> getAllSubordinateBank() {
        //从Redis中查询所属银行
        String SubordinateBankJson = stringRedisTemplate.opsForValue().get("subordinateBank");
        //如果redis里面有所属银行
        if (SubordinateBankJson != null) {
            //将查询出的所属银行json放入返回模型。添加返回成功字段，返回
            return ResponseEntity.ok(new BaseResult(0, "成功").append("data", SubordinateBankJson));
        }
        //如果redis里面没有所属银行
//        查询出所有的所属银行
        List<SubordinateBank> subordinateBanks = subordinateBankService.getAllSubordinateBank();
//        放入redis并且返回
        stringRedisTemplate.opsForValue().set("subordinateBank", JSON.toJSONString(subordinateBanks));
        //将查询出的所属银行放入返回模型。添加返回成功字段，返回
        return ResponseEntity.ok(new BaseResult(0, "成功").append("data", subordinateBanks));
    }

    /**
     * @author: lu
     * @param: * TransferValueVo
     * @return: * ResponseEntity<BaseResult>
     * @description: 单次转账功能
     * @data: 2019/8/12 14:13
     */
    @PostMapping("/verifyBankCardForVo")
    public ResponseEntity<BaseResult> verifyBankCardForVo(@RequestBody TransferValueVo transferValueVo) {
        //对转账金额进行校验
        if(!NumberValid.moneyValid(transferValueVo.getMuchMoney().toString())){
            return ResponseEntity.ok(new BaseResult(1, "金额输入有误，请重新转账"));
        }
        //查询银行卡
        BankCard bankCard = bankCardService.verifyBankCardForVo(transferValueVo);
        if (bankCard == null) {
            return ResponseEntity.ok(new BaseResult(1, "密码错误"));
        }
        if (bankCard.getBankCardBalance().compareTo(transferValueVo.getMuchMoney()) == -1) {
            return ResponseEntity.ok(new BaseResult(1, "余额不足，操作失败"));
        }

        //设置卡号
        transferValueVo.setOutBankCard(bankCard.getBankCardNumber());
        //判断手机为不为空
        if (StringUtils.isNotBlank(transferValueVo.getBankPhone()) && StringUtils.isBlank(transferValueVo.getInBankCard())) {
            BankUser bankUser = userService.getBankUserByUserPhone(transferValueVo.getBankPhone());

            if (StringUtils.isBlank(bankUser.getDefaultBankCard())) {
                return ResponseEntity.ok(new BaseResult(1, "转账失败，收款手机号未绑定银行卡"));
            }
            if (!bankUser.getUserName().equals(transferValueVo.getInBankName())) {
                return ResponseEntity.ok(new BaseResult(1, "转账失败，收款人与银行卡不符合"));
            }
            //添加收款银行卡号
            transferValueVo.setInBankCard(bankUser.getDefaultBankCard());
        }
        if (StringUtils.isBlank(transferValueVo.getBankPhone()) && StringUtils.isNotBlank(transferValueVo.getInBankCard())) {
            if ("BOWR".equals(transferValueVo.getInBank())) {
            //本行卡查询用户进行校验
                Integer userId = bankCardService.selectBankUserByBankCardNum(transferValueVo.getInBankCard());
                BankUser bankUser = userService.selectBankUserByUid(userId);
                if (!bankUser.getUserName().equals(transferValueVo.getInBankName())){
                    return ResponseEntity.ok(new BaseResult(1, "转账失败，收款人与银行卡不符合"));
                }
            }else {
                //模拟调用接口，传输数据给他行，返回他行用户信息
                OtherBankCard otherBankCard = bankCardService.getBankNameByBankNum(transferValueVo.getInBankCard());
                BankUser bankUser = userService.selectBankUserByUid(otherBankCard.getUserId());
                if (!bankUser.getUserName().equals(transferValueVo.getInBankName())){
                    return ResponseEntity.ok(new BaseResult(1, "转账失败，收款人与银行卡不符合"));
                }
            }
        }

//        添加转账记录
        TransferRecord transferRecord = transferRecordService.addTransferRecordforTransferValueVo(transferValueVo);
        if (transferRecord == null) {
            return ResponseEntity.ok(new BaseResult(1, "转账记录生成异常请通知管理员"));
        }
        //操作银行卡扣款 转账状态
        boolean transferStatus = bankCardService.bankCardTransferBusines(transferValueVo);
//          如果转账失败
        if (!transferStatus) {
            boolean transferFailedStatus = transferRecordService.transferFailedOperation(transferRecord);
            if (transferFailedStatus) {
                return ResponseEntity.ok(new BaseResult(1, "转账失败"));
            } else {
                return ResponseEntity.ok(new BaseResult(1, "操作异常请通知管理员"));
            }
        }
        //如果转账成功
        boolean transferSuccessfulStatus = transferRecordService.transferSuccessfulOperation(transferRecord);
        if (transferSuccessfulStatus) {
            return ResponseEntity.ok(new BaseResult(1, "转账成功"));
        } else {
            return ResponseEntity.ok(new BaseResult(1, "转账记录生成异常请通知管理员"));
        }
    }

    /**
     * @author: lu
     * @Param TransferValueVo:
     * @return: ResponseEntity<BaseResult>
     * @description: 根据手机号转账
     * @data: 2019/8/13 14:49
     */
    public ResponseEntity<BaseResult> verifyBankCardForPhone(TransferValueVo transferValueVo) {

        return null;
    }


    /**
     * @author: lu
     * @param: 他行银行卡号
     * @return: ResponseEntity<BaseResult>
     * @description: 根据他行银行卡号查询出所属银行标识
     * @data: 2019/8/13 11:29
     */
    @PostMapping("/selectSubordinateBankByNum")
    public ResponseEntity<BaseResult> selectSubordinateBankByNum(@RequestBody String inBankCard) {

        //如果头四位为9999位本行银行卡
        if ("9999".equals(inBankCard.substring(0, 4))) {
            return ResponseEntity.ok(new BaseResult(0, "成功").append("data", "BOWR"));
        }

        OtherBankCard otherBankCard = bankCardService.getBankNameByBankNum(inBankCard);
        if (otherBankCard==null) {
//           如果为空表示未查询到该卡
            return ResponseEntity.ok(new BaseResult(1, "为找到该卡所属银行，请用户仔细校验卡号"));
        } else {
            return ResponseEntity.ok(new BaseResult(0, "成功").append("data", otherBankCard.getSubordinateBanksIdentification()));
        }
    }
}
