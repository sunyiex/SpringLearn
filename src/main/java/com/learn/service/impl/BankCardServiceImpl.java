package com.learn.service.impl;

import com.learn.domain.BankCard;
import com.learn.repository.BankCardDao;
import com.learn.repository.BankCardOperationLogDao;
import com.learn.service.BankCardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Yi on 2015/6/3.
 */
@Service
public class BankCardServiceImpl implements BankCardService {

    @Resource
    private BankCardDao bankCardDao;
    @Resource
    private BankCardOperationLogDao bankCardOperationLogDao;

    public BankCard findByCardNumber(String cardNumber) {
        return bankCardDao.findByCardNumber(cardNumber);
    }

    public BankCard findLast() {
        List<BankCard> cardList = bankCardDao.findAll();
        if (cardList == null) {
            return null;
        }
        else {
            return cardList.get(cardList.size() - 1);
        }
    }

    public String save(BankCard bankCard) {
        try {
            bankCardDao.save(bankCard);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }
    public String saveAndFlush(BankCard bankCard) {
        try {
            bankCardDao.saveAndFlush(bankCard);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }

    public String save(List<BankCard> bankCard) {
        try {
            bankCardDao.save(bankCard);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }

    public String addOperationLog(BankCard bankCard) {
        try {
            bankCardOperationLogDao.save(bankCard.getOperationList());
            bankCardDao.save(bankCard);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }

    public String delete(BankCard bankCard) {
        try {
            bankCardDao.delete(bankCard);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }
}
