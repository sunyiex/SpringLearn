package com.learn.service.impl;

import com.learn.domain.LoginInLog;
import com.learn.domain.LoginUser;
import com.learn.domain.BankCard;
import com.learn.repository.BankCardDao;
import com.learn.repository.LoginInlogDao;
import com.learn.repository.LoginUserDao;
import com.learn.service.LoginUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Yi on 2015/5/24.
 */
@Service
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class LoginUserServiceImpl implements LoginUserService {

    @Resource
    private LoginUserDao loginUserDao;
    @Resource
    private LoginInlogDao loginInlogDao;
    @Resource
    private BankCardDao bankCardDao;

    /**
     * @param username
     * @return
     */
    public LoginUser findByIDCard(String username) {
        return loginUserDao.findByIDCard(username);
    }

    /**
     * @param loginUser
     * @return
     */
    public String save(LoginUser loginUser) {
        try {
            loginUserDao.save(loginUser);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }

    public String addBankCard(LoginUser loginUser, BankCard bankCard) {
        loginUser.addCardList(bankCard);
        try {
            bankCardDao.save(bankCard);
            loginUserDao.save(loginUser);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }

    public String addLoginInlog(LoginUser loginUser, LoginInLog loginInLog) {
        loginUser.addLoginIglooList(loginInLog);
        try {
            loginInlogDao.save(loginInLog);
            loginUserDao.save(loginUser);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }

    public String deleteBankCard(LoginUser loginUser, BankCard bankCard) {
        loginUser.removeCardList(bankCard);
        try {
            loginUserDao.save(loginUser);
            bankCardDao.delete(bankCard);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }

    public String deleteFristLoginInLog(LoginUser loginUser) {
        LoginInLog loginInLog = loginUser.getLoginIglooList().get(0);
        loginUser.removeLoginIglooList(loginInLog);
        try {
            loginUserDao.save(loginUser);
            loginInlogDao.delete(loginInLog);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }

    public String deleteLoginInLog(LoginUser loginUser, LoginInLog loginInLog) {
        loginUser.removeLoginIglooList(loginInLog);
        try {
            loginUserDao.save(loginUser);
            loginInlogDao.delete(loginInLog);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }

    public String delete(LoginUser loginUser) {
        try {
            loginUserDao.delete(loginUser);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }
}
