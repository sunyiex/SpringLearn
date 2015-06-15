package com.learn.service;

import com.learn.domain.BankCard;
import com.learn.repository.BankCardDao;

/**
 * Created by Yi on 2015/6/3.
 */
public interface BankCardService extends BaseService<BankCard,BankCardDao>{

    public BankCard findByCardNumber(String cardNumber);
    public BankCard findLast();
    public String  save(BankCard bankCard);
    public String delete(BankCard bankCard);
    public String saveAndFlush(BankCard bankCard);
    public String depositOrDraw(BankCard bankCard,String operationType, float money);
    public String transfer(BankCard bankCard, BankCard dstBankCard, float money);

}
