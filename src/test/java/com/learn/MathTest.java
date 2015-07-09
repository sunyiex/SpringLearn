package com.learn;

import com.learn.domain.LoginUser;
import com.learn.domain.BankCard;
import com.learn.common.utils.MD5Util;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private boolean check(String val, String exp){
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(exp);
        matcher = pattern.matcher(val);
        return matcher.find();
    }
    @Test
    public void checkUser(){
        boolean canUse = true;

        String nameRegExp = "^[\\x{4e00}-\\x{9fa5}]{1,15}$";
        String IDCardRegExp = "^[0-9]{17,17}[0-9|X|x]{1,1}$";
        String phoneRegExp =  "^[0-9]{11,11}$";
        String emailRegRxp =  "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        String birthdayRegExp =  "((^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(10|12|0?[13578])([-\\/\\._])(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(11|0?[469])([-\\/\\._])(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(0?2)([-\\/\\._])(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([3579][26]00)([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([1][89][0][48])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][0][48])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([1][89][2468][048])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][2468][048])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([1][89][13579][26])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][13579][26])([-\\/\\._])(0?2)([-\\/\\._])(29)$))";
        String passwordRegExp =  "^[^ˇ|^～]{6,16}$";
        String sexRegExp =  "^(male|female)$";

        org.springframework.util.Assert.isTrue(check("male|female", sexRegExp));
//
    }

    @Test
    public void MD5test() throws NoSuchAlgorithmException {
        String s = "123";
       String a =  MD5Util.MD5(s);
       String b =  MD5Util.MD52(s);
        Assert.assertEquals(a, b);

    }


}
