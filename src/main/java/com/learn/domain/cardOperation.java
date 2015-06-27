package com.learn.domain;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by Yi on 2015/6/3.
 * 请求操作，比如挂失
 */
@Entity
public class CardOperation extends BaseObject{
//  银行卡ID
    private long bankCardId;
//  用户ID
    private long userId;
//    操作
    private String operation;
//    时间
    private Date time;

    public long getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(long bankCardId) {
        this.bankCardId = bankCardId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
