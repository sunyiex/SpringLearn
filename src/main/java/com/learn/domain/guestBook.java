package com.learn.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by Yi on 2015/6/3.
 * 留言板类
 */
@Entity
public class guestBook extends BaseObject{

    @ManyToOne
    @JoinColumn(name="userId")
    //用户的信息
    LoginUser user;
//    留言时间
    private Date time;
//  留言正文
    private String text;

    public LoginUser getUser() {
        return user;
    }

    public void setUser(LoginUser user) {
        this.user = user;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
