package com.learn.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Yi on 2015/5/24.
 * 用户基本信息
 */
@Entity
public class LoginUser extends BaseObject {
//  身份证
    private String IDCard;
//    密码
    private String password;
//    姓名
    private String name;
//    性别
    private String sex;
//    电话
    private String phone;
//    电子邮箱
    private String email;
//    账户类型
    private String type;
//    出生日期
    private Date birthday;
//    注册时间
    private Date Time;
//    银行卡号
    @OneToMany(cascade= CascadeType.ALL)
    private List<BankCard> cardList;
//    登录记录
    @OneToMany
    private List<LoginInLog> loginIglooList;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }
    public List<BankCard> getCardList() {
        return cardList;
    }

    public void setCardList(List<BankCard> cardList) {
        this.cardList = cardList;
    }

    public void addCardList(BankCard card) {
        if(this.cardList!=null){
            this.cardList.add(card);
        }
        else{
            this.cardList = new ArrayList<BankCard>();
            this.cardList.add(card);
        }
    }
    public void removeCardList(BankCard card) {
        this.cardList.remove(card);
    }

    public List<LoginInLog> getLoginIglooList() {
        return loginIglooList;
    }

    public void setLoginIglooList(List<LoginInLog> loginIglooList) {
        this.loginIglooList = loginIglooList;
    }
    public void addLoginIglooList(LoginInLog loginInLog) {
        if(this.loginIglooList!=null){
            this.loginIglooList.add(loginInLog);
        }
        else{
            this.loginIglooList = new ArrayList<LoginInLog>();
            this.loginIglooList.add(loginInLog);
        }
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }
}
