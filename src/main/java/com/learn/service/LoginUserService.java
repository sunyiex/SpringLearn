package com.learn.service;

import com.learn.domain.BankCard;
import com.learn.domain.LoginInLog;
import com.learn.domain.LoginUser;
import com.learn.repository.LoginUserDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;


/**
 * Created by Yi on 2015/5/24.
 */
public interface LoginUserService extends BaseService<LoginUser,LoginUserDao>{

    public LoginUser findByIDCard(String username);
    public String save(LoginUser loginUser);
    public String delete(LoginUser loginUser);
    public String addBankCard(LoginUser loginUser, BankCard bankCard);
    public String addLoginInlog(LoginUser loginUser, LoginInLog loginInLog);
    public String deleteBankCard(LoginUser loginUser,BankCard bankCard);
    public String deleteLoginInLog(LoginUser loginUser,LoginInLog loginInLog);
    public String deleteFristLoginInLog(LoginUser loginUser);
    public String checkUser(LoginUser loginUser, String birthday);
    public String checkUser(LoginUser loginUser);
    public Page<LoginUser> findAll(PageRequest pageRequest);
    public List<LoginUser> findAll();
    public String sendEmail(String toEmail, String IDCard, long userId, String URL);
}
