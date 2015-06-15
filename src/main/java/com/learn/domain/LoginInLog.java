package com.learn.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by Yi on 2015/5/29.
 * 用户登陆记录
 */
@Entity
public class LoginInLog extends BaseObject{
//    登陆时间
    private Date time;
//    登陆IP
    private String ip;

    private  String address;
//  登陆账户
    @ManyToOne
    @JoinColumn(name = "userId")
    private LoginUser loginUser;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LoginUser getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(LoginUser loginUser) {
        this.loginUser = loginUser;
    }
}
