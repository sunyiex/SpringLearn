package com.learn.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
    private float operationMoney;
//    操作时间
    private Date time;

    @OneToOne
    private BankCard dstBankCard;
    @OneToOne
    private BankCard srcBankCard;


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

    public void setOperationMoney(float operationMoney) {
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
    public BankCard getDstBankCard() {
        return dstBankCard;
    }

    public void setDstBankCard(BankCard dstBankCard) {
        this.dstBankCard = dstBankCard;
    }

    public BankCard getSrcBankCard() {
        return srcBankCard;
    }

    public void setSrcBankCard(BankCard srcBankCard) {
        this.srcBankCard = srcBankCard;
    }
}
