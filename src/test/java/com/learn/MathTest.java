package com.learn;

import com.learn.domain.LoginUser;
import com.learn.domain.BankCard;
import com.learn.common.utils.MD5Util;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Yi on 2015/5/23.
 */
public class MathTest {

    private Math mathTest;

    @Before
    public void setuUp(){
        mathTest = new Math();
    }

    @Test
    public void plusTest(){
        int request = mathTest.plus(3, 2);

        Assert.assertEquals(5, request);
    }

    @Test
    public void MD5UtilTest(){
        String str = "123456461";
        String strmd5 = MD5Util.MD5(str);
        Assert.assertEquals(str, strmd5);
    }
    @Test
    public void listTest(){
        LoginUser loginUser =  new LoginUser();
        BankCard bankCard1 = new BankCard();
        BankCard bankCard2 = new BankCard();
        BankCard bankCard3 = new BankCard();
        BankCard bankCard4 = new BankCard();
        bankCard1.setCardNumber("1");
        bankCard2.setCardNumber("2");
        bankCard3.setCardNumber("3");
        bankCard4.setCardNumber("4");

        loginUser.addCardList(bankCard1);
        loginUser.addCardList(bankCard2);
        loginUser.addCardList(bankCard3);
        loginUser.addCardList(bankCard4);

        loginUser.removeCardList(bankCard1);
        loginUser.removeCardList(bankCard3);
        loginUser.removeCardList(bankCard2);

    }


}
