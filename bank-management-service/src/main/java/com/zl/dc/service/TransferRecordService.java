package com.zl.dc.service;

import com.zl.dc.mapper.TransferRecordMapper;
import com.zl.dc.mapper.UserMapper;
import com.zl.dc.pojo.BankUser;
import com.zl.dc.pojo.TransferRecord;
import com.zl.dc.util.AccessBank;
import com.zl.dc.vo.PageBean;
import com.zl.dc.vo.TransferValueVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @version: V1.2
 * @author: lu
 * @className: TransferRecordService
 * @description: 转账记录操作层
 * @data: 2019/8/12
 */
@Service
@Transactional
public class TransferRecordService {
    @Resource
    private TransferRecordMapper transferRecordMapper;
    @Resource
    private TransferRecord transferRecord;
    @Resource
    private UserMapper userMapper;

    /**
     * @author: lu
     * @param: * TransferValueVo
     * @return: * boolean
     * @description: 根据单次转账VO添加转账记录
     * @data: 2019/8/12 15:57
     */
    public TransferRecord addTransferRecordforTransferValueVo(TransferValueVo transferValueVo) {
        if (transferValueVo == null) {
            return null;
        }
        transferRecord.setTransferRecordUuid(UUID.randomUUID().toString().replaceAll("-", ""));
        if (transferValueVo.getMuchMoney() != null) {
            transferRecord.setTransferRecordAmount(transferValueVo.getMuchMoney());
        }
        transferRecord.setTransferRecordTime(null);
        if (StringUtils.isNotBlank(transferValueVo.getTransferRemarks())) {
            transferRecord.setTransferNote(transferValueVo.getTransferRemarks());
        }
        transferRecord.setTransferType(Byte.parseByte("100"));
        transferRecord.setTransferStatus(Byte.parseByte("110"));
        if (transferValueVo.getUserId() != null) {
            transferRecord.setUserId(transferValueVo.getUserId());
        }
        if (StringUtils.isNotBlank(transferValueVo.getOutBankCard())) {
            transferRecord.setBankOutCard(transferValueVo.getOutBankCard());
        }
        if (StringUtils.isNotBlank(transferValueVo.getInBankName())) {
            transferRecord.setInCardUserName(transferValueVo.getInBankName());
        }
        if (StringUtils.isNotBlank(transferValueVo.getInBank())) {
            transferRecord.setBankInIdentification(transferValueVo.getInBank());
        }
        if (StringUtils.isNotBlank(transferValueVo.getInBankCard())) {
            transferRecord.setBankInCard(transferValueVo.getInBankCard());
        }
        transferRecord.setGmtCreate(new Date());
        transferRecord.setGmtModified(new Date());
        //添加转账记录
        transferRecordMapper.insertSelective(transferRecord);
        //拼接查询条件
        Example example = new Example(TransferRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("transferRecordUuid", transferRecord.getTransferRecordUuid());
        return transferRecordMapper.selectOneByExample(example);

    }

    /**
     * @author: lu
     * @param: * TransferRecord
     * @return: * boolean
     * @description: 转账失败时修改转账记录的方法
     * @data: 2019/8/12 17:00
     */
    public boolean transferFailedOperation(TransferRecord transferRecord) {
        transferRecord.setTransferStatus(Byte.parseByte("130"));
        transferRecord.setTransferRecordTime(new Date());
        transferRecord.setGmtModified(new Date());
        int status = transferRecordMapper.updateByPrimaryKeySelective(transferRecord);
        if (status > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @author: lu
     * @param: * TransferRecord
     * @return: * boolean
     * @description: 转账成功时时修改转账记录的方法
     * @data: 2019/8/12 17:00
     */
    public boolean transferSuccessfulOperation(TransferRecord transferRecord) {
        transferRecord.setTransferStatus(Byte.parseByte("120"));
        transferRecord.setTransferRecordTime(new Date());
        transferRecord.setGmtModified(new Date());
        int status = transferRecordMapper.updateByPrimaryKeySelective(transferRecord);
        if (status > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @author: redsheep
     * @Param pageBean
     * @return: java.util.List<com.zl.dc.pojo.TransferRecord>
     * @description: 根据月份分页查询用户所有卡的转账记录
     * @data: 2019/8/13 9:26
     */
    public List<TransferRecord> getTransferRecordByMonth(PageBean pageBean) {

        return null;
    }
}
