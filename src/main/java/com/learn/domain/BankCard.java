package com.learn.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Yi on 2015/5/28.
 * 银行卡类
 */
@Entity
public class BankCard  extends BaseObject{
//  银行卡号
    private String cardNumber;
//    余额
    private float balance;
//    创建时间
    private Date time;
//    卡号状态
    private String activeFlag;
//  操作记录
    @OneToMany
    private List<BankCardOperationLog> operationList;
//  卡号持有者
    @ManyToOne
    @JoinColumn(name = "userId")
    private LoginUser loginUser;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public LoginUser getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(LoginUser loginUser) {
        this.loginUser = loginUser;
    }
    public List<BankCardOperationLog> getOperationList() {
        return operationList;
    }

    public void setOperationList(List<BankCardOperationLog> operationList) {
        this.operationList = operationList;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }
}
