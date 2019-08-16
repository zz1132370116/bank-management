package com.zl.dc.controller;

import com.alibaba.fastjson.JSON;
import com.zl.dc.pojo.BankEnterprise;
import com.zl.dc.service.BankEnterpriseService;
import com.zl.dc.service.TransferRecordService;
import com.zl.dc.util.MD5;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            String password = MD5.GetMD5Code(bankEnterprise.getEnterpriseLoginPassword());
            if (password.equals(enterpriseBankCard.getEnterpriseLoginPassword())){
                stringRedisTemplate.opsForValue().set(enterpriseBankCard.getEnterpriseName(), JSON.toJSONString(bankEnterprise));
                enterpriseBankCard.setGmtCreate(null);
                enterpriseBankCard.setGmtModified(null);
                return new BaseResult(0,"登录成功").append("enterprise",enterpriseBankCard);
            }
        }
        return new BaseResult(1,"登录失败，账号或密码错误");
    }

    @PostMapping("/batchImport")
    public ResponseEntity<BaseResult> batchImport(@RequestParam("file") MultipartFile file,@RequestParam("enterpriseId") Integer enterpriseId) throws IOException {
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

            if(!StringUtils.isNoneBlank(row.getCell(0).getStringCellValue(),row.getCell(1).getStringCellValue(),row.getCell(3).getStringCellValue())){
                return ResponseEntity.ok(new BaseResult(1,"名字，银行卡号，金额不能为空"));
            }

            String userName = row.getCell(0).getStringCellValue();
            String userBankCardNumber = row.getCell(1).getStringCellValue();
            String userBankCardName = row.getCell(2).getStringCellValue();
            String moneyStr = row.getCell(3).getStringCellValue();

            BigDecimal money = new BigDecimal(moneyStr);

            //将数据封装到EnterpriseEmployee对象
            EnterpriseEmployee enterpriseEmployee = new EnterpriseEmployee();
            enterpriseEmployee.setUserName(userName);
            enterpriseEmployee.setUserBankCardName(userBankCardName);
            enterpriseEmployee.setUserBankCardNumber(userBankCardNumber);
            enterpriseEmployee.setMoney(money);

            //添加数据到集合
            enterpriseEmployeeList.add(enterpriseEmployee);
        }
        //删除和关闭
        workbook.close();
        newFile.delete();

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
        List<EnterpriseEmployee> enterpriseEmployees = enterpriseEmployeeVo.getEnterpriseEmployees();
        for (EnterpriseEmployee enterpriseEmployee : enterpriseEmployees) {
            Integer insert = transferRecordService.addTransferRecordDueToBankEnterprise(enterpriseEmployee);
            if (insert == 0){
                enterpriseEmployee.setStatus("失败");
            } else {
                enterpriseEmployee.setStatus("成功");
            }
        }

        return ResponseEntity.ok(new BaseResult(0,"成功").append("data",enterpriseEmployees));
    }
}
