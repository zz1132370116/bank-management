package com.zl.dc.vo;

import lombok.Data;

import java.util.List;

/**
 * @version V1.0
 * @author pds
 * @className EnterpriseEmployeeVo
 * @description TODO
 * @date 2019/8/16 16:48
 */
@Data
public class EnterpriseEmployeeVo {
    private List<EnterpriseEmployee> enterpriseEmployees;
    private Integer enterpriseId;
}
