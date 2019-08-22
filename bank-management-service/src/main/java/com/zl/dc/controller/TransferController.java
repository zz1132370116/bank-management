package com.zl.dc.controller;

import com.alibaba.fastjson.JSON;
import com.zl.dc.api.AccessBank;
import com.zl.dc.config.RedisInsertUtil;
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

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @version: V1.0
 * @author: lu
 * @className: TransferController
 * @description: 单次转账控制层
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
        if (!NumberValid.moneyValid(transferValueVo.getMuchMoney().toString())) {
            return ResponseEntity.ok(new BaseResult(1, "金额输入有误，请重新转账"));
        }
        //查询银行卡
        BankCard bankCard = bankCardService.selectBankCardByid(transferValueVo.getOutBankCardID());
        if (bankCard == null) {
            return ResponseEntity.ok(new BaseResult(1, "未找到用户操作卡，请联系管理员"));
        }
        //密码错误次数达到3次之后会暂时将冻结，冻结今天的剩余时间，即在过了24:00之后就可以登录，在冻结时间之内不允许该用户登录
        //查询redies该卡是否被锁定
        String timesStr = stringRedisTemplate.opsForValue().get("BankCard" + bankCard.getBankCardId());
        if (timesStr != null && timesStr.equals("3")) {
            return ResponseEntity.ok(new BaseResult(1, "您今天的输错密码达到上限，请明天再试，或者选择忘记密码"));
        }

        boolean passwordStatus = bankCardService.BankCardPasswordCheck(bankCard, transferValueVo.getPassword());
//       当输错密码时
        if (!passwordStatus) {
            //调用方法在redis缓存中存入错误信息
            RedisInsertUtil.addingData(stringRedisTemplate, "BankCard" + bankCard.getBankCardId(), timesStr);
            return ResponseEntity.ok(new BaseResult(1, "密码输入错误，每日限制3次"));
        }
        if (!"100".equals(bankCard.getBankCardStatus().toString())) {
            return ResponseEntity.ok(new BaseResult(1, "银行卡状态异常，请查看该卡状态"));
        }
        if (bankCard.getBankCardBalance().compareTo(transferValueVo.getMuchMoney()) == -1) {
            return ResponseEntity.ok(new BaseResult(1, "余额不足，操作失败"));
        }
        if (bankCard.getBankCardTransferLimit() < transferValueVo.getMuchMoney().intValue()) {
            return ResponseEntity.ok(new BaseResult(1, "交易被限额，操作失败"));
        }

        //设置转出卡号
        transferValueVo.setOutBankCard(bankCard.getBankCardNumber());

        //如果手机为不为空，银行卡号为空
        if (StringUtils.isNotBlank(transferValueVo.getBankPhone()) && StringUtils.isBlank(transferValueVo.getInBankCard())) {
            //根据手机查询用户
            BankUser bankUser = userService.getBankUserByUserPhone(transferValueVo.getBankPhone());

            if (StringUtils.isBlank(bankUser.getDefaultBankCard())) {
                return ResponseEntity.ok(new BaseResult(1, "转账失败，收款手机号未绑定银行卡"));
            }
            if (!bankUser.getUserName().equals(transferValueVo.getInBankName())) {
                return ResponseEntity.ok(new BaseResult(1, "转账失败，收款人姓名不符合不符合"));
            }
            //添加收款银行卡号
            transferValueVo.setInBankCard(bankUser.getDefaultBankCard());
            transferValueVo.setInBank("BOWR");
        }


//        添加转账记录
        TransferRecord transferRecord = transferRecordService.addTransferRecordforTransferValueVo(transferValueVo);
        if (transferRecord == null) {
            return ResponseEntity.ok(new BaseResult(1, "转账记录生成异常请通知管理员"));
        }
        //如果手机为为空，银行卡号不为空
        if (StringUtils.isBlank(transferValueVo.getBankPhone()) && StringUtils.isNotBlank(transferValueVo.getInBankCard())) {
            //银行卡姓名校验
            boolean nameCheck = bankCardService.checkNameAndBankCard(transferValueVo.getInBankName(), transferValueVo.getInBankCard());
            if (nameCheck) {
                //操作银行卡扣款 转账状态
                boolean transferStatus = bankCardService.bankCardTransferBusines(transferValueVo.getOutBankCardID(), transferValueVo.getInBankCard(), transferValueVo.getMuchMoney());
                if (transferStatus) {
                    //设置转账记录为成功状态
                    boolean transferSuccessfulStatus = transferRecordService.transferSuccessfulOperation(transferRecord);
                    if (transferSuccessfulStatus) {
                        return ResponseEntity.ok(new BaseResult(0, "转账提交成功，请稍后查询"));
                    } else {
                        return ResponseEntity.ok(new BaseResult(1, "操作异常请通知管理员"));
                    }
                } else {
                    return ResponseEntity.ok(new BaseResult(1, "银行卡转账异常，请通知管理员"));
                }
            } else {
                //设置转账记录为失败状态
                boolean transferFailedStatus = transferRecordService.transferFailedOperation(transferRecord);
                if (transferFailedStatus) {
                    return ResponseEntity.ok(new BaseResult(0, "转账提交成功，请稍后查询"));
                } else {
                    return ResponseEntity.ok(new BaseResult(1, "操作异常请通知管理员"));
                }
            }
        } else {
            //操作银行卡扣款 转账状态
            boolean transferStatus = bankCardService.bankCardTransferBusines(transferValueVo.getOutBankCardID(), transferValueVo.getInBankCard(), transferValueVo.getMuchMoney());
            if (transferStatus) {
                //设置转账记录为成功状态
                boolean transferSuccessfulStatus = transferRecordService.transferSuccessfulOperation(transferRecord);
                if (transferSuccessfulStatus) {
                    return ResponseEntity.ok(new BaseResult(0, "转账提交成功，请稍后查询"));
                } else {
                    return ResponseEntity.ok(new BaseResult(1, "操作异常请通知管理员"));
                }
            } else {
                //设置转账记录为失败状态
                boolean transferFailedStatus = transferRecordService.transferFailedOperation(transferRecord);
                if (transferFailedStatus) {
                    return ResponseEntity.ok(new BaseResult(0, "转账提交成功，请稍后查询"));
                } else {
                    return ResponseEntity.ok(new BaseResult(1, "操作异常请通知管理员"));
                }
            }
        }
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
        if (otherBankCard == null) {
//           如果为空表示未查询到该卡
            return ResponseEntity.ok(new BaseResult(1, "为找到该卡所属银行，请用户仔细校验卡号"));
        } else {
            return ResponseEntity.ok(new BaseResult(0, "成功").append("data", otherBankCard.getSubordinateBanksIdentification()));
        }
    }
}
