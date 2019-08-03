package com.zl.dc.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "subordinate_banks")
public class SubordinateBanks {
    /**
     * 银行id
     */
    @Id
    @Column(name = "subordinate_banks_id")
    private Integer subordinateBanksId;
    /**
     * 银行标识
     */
    @Column(name = "subordinate_banks_identification")
    private String subordinateBanksIdentification;
    /**
     * 银行名称
     */
    @Column(name = "subordinate_banks_name")
    private String subordinateBanksName;

    public SubordinateBanks(Integer subordinateBanksId, String subordinateBanksIdentification, String subordinateBanksName) {
        this.subordinateBanksId = subordinateBanksId;
        this.subordinateBanksIdentification = subordinateBanksIdentification;
        this.subordinateBanksName = subordinateBanksName;
    }

    public SubordinateBanks() {
        super();
    }

    public Integer getSubordinateBanksId() {
        return subordinateBanksId;
    }

    public void setSubordinateBanksId(Integer subordinateBanksId) {
        this.subordinateBanksId = subordinateBanksId;
    }

    public String getSubordinateBanksIdentification() {
        return subordinateBanksIdentification;
    }

    public void setSubordinateBanksIdentification(String subordinateBanksIdentification) {
        this.subordinateBanksIdentification = subordinateBanksIdentification == null ? null : subordinateBanksIdentification.trim();
    }

    public String getSubordinateBanksName() {
        return subordinateBanksName;
    }

    public void setSubordinateBanksName(String subordinateBanksName) {
        this.subordinateBanksName = subordinateBanksName == null ? null : subordinateBanksName.trim();
    }
}