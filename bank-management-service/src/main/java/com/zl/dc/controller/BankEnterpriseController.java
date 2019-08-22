package com.zl.dc.controller;

import com.alibaba.fastjson.JSON;
import com.zl.dc.api.AccessBank;
import com.zl.dc.config.RedisInsertUtil;
import com.zl.dc.pojo.BankCard;
import com.zl.dc.pojo.BankEnterprise;
import com.zl.dc.service.BankCardService;
import com.zl.dc.service.BankEnterpriseService;
import com.zl.dc.service.TransferRecordService;
import com.zl.dc.util.MD5;
import com.zl.dc.util.NumberValid;
import com.zl.dc.util.StringValid;
import com.zl.dc.vo.BaseResult;
import com.zl.dc.vo.EnterpriseEmployee;
import com.zl.dc.vo.EnterpriseEmployeeVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @version V1.0
 * @author pds
 * @className BankEnterpriseController
 * @description 企业控制层
 * @date 2019/8/15 19:58
 */
@RestController
public class BankEnterpriseController {
    @Resource
    private BankEnterpriseService bankEnterpriseService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private TransferRecordService transferRecordService;
    @Resource
    private BankCardService bankCardService;

    /**
     * @author pds
     * @param bankEnterprise
     * @return com.zl.dc.vo.BaseResult
     * @description 企业登录
     * @date 2019/8/16 10:18
     */
    @PostMapping("/enterpiseLogin")
    public BaseResult enterpiseLogin(@RequestBody BankEnterprise bankEnterprise){
        BankEnterprise enterpriseBankCard = bankEnterpriseService.getBankEnterpriseByEnterpriseBankCard(bankEnterprise.getEnterpriseBankCard());
        if (enterpriseBankCard != null){
            //密码错误次数达到3次之后会暂时将账号冻结，冻结今天的剩余时间，即在过了24:00之后就可以登录，在冻结时间之内不允许该用户登录
            // 这里先从redis里查该用户是否被禁止登录
            String timesStr = stringRedisTemplate.opsForValue().get(enterpriseBankCard.getEnterpriseName()+"-"+enterpriseBankCard.getEnterpriseId());
            if (timesStr != null && timesStr.equals("3")){
                return new BaseResult(1,"您今天的输错密码的次数为3次，请明天再试，或者选择忘记密码");
            }

            String password = MD5.GetMD5Code(bankEnterprise.getEnterpriseLoginPassword());
            if (password.equals(enterpriseBankCard.getEnterpriseLoginPassword())){
                stringRedisTemplate.opsForValue().set(enterpriseBankCard.getEnterpriseName(), JSON.toJSONString(bankEnterprise));
                stringRedisTemplate.opsForValue().set("enterprise-"+enterpriseBankCard.getEnterpriseId()+"-enterpriseInfo", JSON.toJSONString(bankEnterprise));
                enterpriseBankCard.setGmtCreate(null);
                enterpriseBankCard.setGmtModified(null);
                return new BaseResult(0,"登录成功").append("enterprise",enterpriseBankCard);
            } else {
                Integer times = RedisInsertUtil.addingData(stringRedisTemplate, enterpriseBankCard.getEnterpriseName() + "-" + enterpriseBankCard.getEnterpriseId(), timesStr);
                return new BaseResult(1,"输错密码"+times+"次，只有"+(3-times)+"次机会了");
            }
        }
        return new BaseResult(1,"登录失败，账号或密码错误");
    }

    @PostMapping("/batchImport")
    public ResponseEntity<BaseResult> batchImport(@RequestParam("file") MultipartFile file,@RequestParam("enterpriseId") Integer enterpriseId) throws IOException {
        if (!StringUtils.isNotBlank(file.getOriginalFilename())){
            return ResponseEntity.ok(new BaseResult(1,"请选择.xls文件"));
        }
        if (enterpriseId == null || enterpriseId <= 0){
            return ResponseEntity.ok(new BaseResult(1,"文件解析失败"));
        }

        long currentTimeMillis = System.currentTimeMillis();
        BankEnterprise bankEnterprise = bankEnterpriseService.getBankEnterpriseById(enterpriseId);
        if (bankEnterprise == null){
            return ResponseEntity.ok(new BaseResult(1,"文件解析失败"));
        }
        //1 处理excel文件 （注意：必须上传提供模板）
        //1.1 excel文件保存到服务器
        File newFile = new File("D:\\upload" , UUID.randomUUID() + file.getOriginalFilename());
        file.transferTo(newFile);
        //1.2 解析excel文件
        // 1) 加载 xls 文件 获得工作簿（Workbook ，Ctrl + H 查看接口的实现类，从而编写出HSSFWorkBook类）
        Workbook workbook = null;
        try {
            workbook = new HSSFWorkbook( new FileInputStream(newFile));
        } catch (OfficeXmlFileException e){
            e.printStackTrace();
            return ResponseEntity.ok(new BaseResult(1,"请选择.xls文件"));
        }
        // 2) 获得第一张表
        Sheet sheet = workbook.getSheetAt(0);
        // 3) 获得所有的行
        int lastRow = sheet.getLastRowNum();
        // 存放所有解析后的area对象的
        List<EnterpriseEmployee> enterpriseEmployeeList = new ArrayList<>();
        for(int i = 1 ; i <= lastRow ; i ++){
            // 4) 获得对应单元格
            Row row = sheet.getRow(i);

            row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);

            if(!StringValid.isBlank(row.getCell(0).getStringCellValue(),row.getCell(1).getStringCellValue(),row.getCell(3).getStringCellValue())){
                return ResponseEntity.ok(new BaseResult(1,"名字，银行卡号，金额不能为空"));
            }

            String userName = row.getCell(0).getStringCellValue();
            String userBankCardNumber = row.getCell(1).getStringCellValue();
            String userBankCardName = row.getCell(2).getStringCellValue();
            String moneyStr = row.getCell(3).getStringCellValue();
            BigDecimal money = new BigDecimal(moneyStr);

            String identify = "";
            //获取银行卡的标识
            try {
                identify = AccessBank.getSubordinateBank(userBankCardNumber);
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            //将数据封装到EnterpriseEmployee对象
            EnterpriseEmployee enterpriseEmployee = new EnterpriseEmployee();
            enterpriseEmployee.setUserName(userName);
            enterpriseEmployee.setUserBankCardName(userBankCardName);
            enterpriseEmployee.setUserBankCardNumber(userBankCardNumber);
            enterpriseEmployee.setBankInIdentification(identify);
            enterpriseEmployee.setMoney(money);

            //添加数据到集合
            enterpriseEmployeeList.add(enterpriseEmployee);
        }

        //删除和关闭
        workbook.close();
        newFile.delete();

        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println("zhu耗时:"+(currentTimeMillis1-currentTimeMillis)+"ms");
        return ResponseEntity.ok(new BaseResult(0,"成功").append("data",enterpriseEmployeeList));
    }


    /**
     * @author pds
     * @param enterpriseEmployeeVo
     * @return org.springframework.http.ResponseEntity<com.zl.dc.vo.BaseResult>
     * @description 企业批量转账
     * @date 2019/8/16 17:06
     */
    @PostMapping("/enterpriseTransfer")
    public ResponseEntity<BaseResult> enterpriseTransfer(@RequestBody EnterpriseEmployeeVo enterpriseEmployeeVo){
        BankEnterprise bankEnterprise = bankEnterpriseService.getBankEnterpriseById(enterpriseEmployeeVo.getEnterpriseId());
        if (bankEnterprise == null){
            return ResponseEntity.ok(new BaseResult(1,"转账失败"));
        }
        BankCard bankCard = bankCardService.selectBankCardByid(bankEnterprise.getEnterpriseBankCardId());

        if (!MD5.GetMD5Code(enterpriseEmployeeVo.getPassword()).equals(bankCard.getBankCardPassword())) {
            return ResponseEntity.ok(new BaseResult(1, "密码错误"));
        }
        if (!"100".equals(bankCard.getBankCardStatus().toString())) {
            return ResponseEntity.ok(new BaseResult(1, "账户状态异常"));
        }

        BigDecimal mount = new BigDecimal(0);
        for (EnterpriseEmployee enterpriseEmployee : enterpriseEmployeeVo.getEnterpriseEmployees()) {
            if (!NumberValid.moneyValid(enterpriseEmployee.getMoney().toString())) {
                return ResponseEntity.ok(new BaseResult(1, enterpriseEmployee.getUserName()+"的转账金额异常"));
            }
            mount.add(enterpriseEmployee.getMoney());
        }
        if (bankCard.getBankCardBalance().compareTo(mount) == -1){
            return ResponseEntity.ok(new BaseResult(1,"公司账户余额不足"));
        }

        List<EnterpriseEmployee> enterpriseEmployeeList = transferRecordService.addTransferRecordDueToBankEnterprise(enterpriseEmployeeVo.getEnterpriseEmployees(), bankEnterprise);

        return ResponseEntity.ok(new BaseResult(0,"转账成功").append("data",enterpriseEmployeeList));
    }

    @GetMapping("/enterpiseSignOut")
    public BaseResult enterpiseSignOut(@RequestParam("enterpiseId") Integer enterpiseId){
        BankEnterprise bankEnterprise = bankEnterpriseService.getBankEnterpriseById(enterpiseId);
        if (bankEnterprise == null) {
            return new BaseResult(1, "退出登录失败");
        }
        Boolean delete = stringRedisTemplate.delete(bankEnterprise.getEnterpriseName());
        if (delete){
            return new BaseResult(0, "退出登录成功");
        }
        return new BaseResult(1, "退出登录失败");
    }
}
