package com.learn.service.impl;

import com.learn.domain.BankCard;
import com.learn.domain.BankCardOperationLog;
import com.learn.repository.BankCardDao;
import com.learn.repository.BankCardOperationLogDao;
import com.learn.service.BankCardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by Yi on 2015/6/3.
 */
@Service
// 类中所有public函数都纳入事务管理的标识.
@Transactional
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

    public String addOperationLog(BankCard bankCard,BankCardOperationLog bankCardOperationLog) {
        bankCard.getOperationList().add(bankCardOperationLog);
        try {
            bankCardOperationLogDao.save(bankCard.getOperationList());
            bankCardDao.save(bankCard);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }
    public List<BankCard> findAll(){
        return bankCardDao.findAll();
    }
    public Page<BankCard> findAll(PageRequest pageRequest){
        return bankCardDao.findAll(pageRequest);
    }
    public String delete(BankCard bankCard) {
        try {
            bankCardDao.delete(bankCard);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }
    public String depositOrDraw(BankCard bankCard, String operationType, float money) {
        BankCardOperationLog bankCardOperationLog = new BankCardOperationLog();
        bankCardOperationLog.setBankCard(bankCard);
        bankCardOperationLog.setTime(new Date());
        bankCardOperationLog.setOperationMoney(money);
        bankCardOperationLog.setOperationType(operationType);
        if(!bankCard.getActiveFlag().equals("normal")){
            return "卡号目前状态不能进行该操作";
        }
        if(operationType.equals("deposit")){
            bankCard.setBalance(money + bankCard.getBalance());
        }
        else if(operationType.equals("draw")){
            if(money > bankCard.getBalance()){
                return "余额不足";
            }
            bankCard.setBalance(bankCard.getBalance() - money);
        }
        else{
            return "操作类型错误";
        }
        return this.addOperationLog(bankCard,bankCardOperationLog);
    }
    public String transfer(BankCard bankCard, BankCard dstBankCard, float money) {
        BankCardOperationLog srcbankCardOperationLog = new BankCardOperationLog();
        srcbankCardOperationLog.setBankCard(bankCard);
        srcbankCardOperationLog.setTime(new Date());
        srcbankCardOperationLog.setOperationMoney(money);
        srcbankCardOperationLog.setOperationType("transferOut");
        srcbankCardOperationLog.setDstBankCard(dstBankCard);
        srcbankCardOperationLog.setSrcBankCard(bankCard);

        BankCardOperationLog dstbankCardOperationLog = new BankCardOperationLog();
        dstbankCardOperationLog.setBankCard(bankCard);
        dstbankCardOperationLog.setTime(new Date());
        dstbankCardOperationLog.setOperationMoney(money);
        dstbankCardOperationLog.setOperationType("transferIn");
        dstbankCardOperationLog.setDstBankCard(dstBankCard);
        dstbankCardOperationLog.setSrcBankCard(bankCard);
        if(!dstBankCard.getActiveFlag().equals("normal")){
            return "要存入的银行卡状态异常，目前不能进行该操作";
        }
        else if(!bankCard.getActiveFlag().equals("normal")){
            return "卡号目前状态不能进行该操作";
        }
        else if(bankCard.getBalance() < money){
            return "余额不足，不能转账！";
        }
        bankCard.setBalance(bankCard.getBalance() - money);
        dstBankCard.setBalance(dstBankCard.getBalance() + money);
        String msg1 = this.addOperationLog(bankCard, srcbankCardOperationLog);
        String msg2 = this.addOperationLog(dstBankCard, dstbankCardOperationLog);
        if(msg1.equals(msg2) && msg1.equals("success")){
            return msg1;
        }
        else{
            return msg2;
        }
    }
}
