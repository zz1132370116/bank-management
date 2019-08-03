package com.zl.dc.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "bank_card")
public class BankCard {
    @Id
    @Column(name = "bank_card_id")
    private Integer bankCardId;
@Column(name = "bank_card_number")
    private String bankCardNumber;
@Column(name = "bank_card_password")
    private String bankCardPassword;
@Column(name = "user_id")
    private Integer userId;
@Transient
    private BankUser bankUser;
@Column(name = "bank_card_balance")
    private Double bankCardBalance;
@Column(name = "subordinate_banks_id")
    private Integer subordinateBanksId;
@Column(name = "bank_card_status")
    private Integer bankCardStatus;

    public BankCard(Integer bankCardId, String bankCardNumber, String bankCardPassword, Integer userId, Double bankCardBalance, Integer subordinateBanksId, Integer bankCardStatus) {
        this.bankCardId = bankCardId;
        this.bankCardNumber = bankCardNumber;
        this.bankCardPassword = bankCardPassword;
        this.userId = userId;
        this.bankCardBalance = bankCardBalance;
        this.subordinateBanksId = subordinateBanksId;
        this.bankCardStatus = bankCardStatus;
    }

    public BankCard() {
        super();
    }

    public Integer getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(Integer bankCardId) {
        this.bankCardId = bankCardId;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber == null ? null : bankCardNumber.trim();
    }

    public String getBankCardPassword() {
        return bankCardPassword;
    }

    public void setBankCardPassword(String bankCardPassword) {
        this.bankCardPassword = bankCardPassword == null ? null : bankCardPassword.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getBankCardBalance() {
        return bankCardBalance;
    }

    public void setBankCardBalance(Double bankCardBalance) {
        this.bankCardBalance = bankCardBalance;
    }

    public Integer getSubordinateBanksId() {
        return subordinateBanksId;
    }

    public void setSubordinateBanksId(Integer subordinateBanksId) {
        this.subordinateBanksId = subordinateBanksId;
    }

    public Integer getBankCardStatus() {
        return bankCardStatus;
    }

    public void setBankCardStatus(Integer bankCardStatus) {
        this.bankCardStatus = bankCardStatus;
    }
}