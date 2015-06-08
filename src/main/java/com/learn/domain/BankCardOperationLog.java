package com.learn.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by Yi on 2015/5/29.
 * 银行卡操作记录
 */
@Entity
public class BankCardOperationLog extends BaseObject{
//    操作类型
    private String operationType;
//    操作金额
    private double operationMoney;
//    操作时间
    private Date time;
//  被操作的银行卡
    @ManyToOne
    @JoinColumn(name = "cardId")
    private BankCard bankCard;

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public double getOperationMoney() {
        return operationMoney;
    }

    public void setOperationMoney(double operationMoney) {
        this.operationMoney = operationMoney;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public BankCard getBankCard() {
        return bankCard;
    }

    public void setBankCard(BankCard bankCard) {
        this.bankCard = bankCard;
    }

}
