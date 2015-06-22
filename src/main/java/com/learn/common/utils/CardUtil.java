package com.learn.common.utils;

/**
 * Created by Yi on 2015/6/17.
 */
public class CardUtil {

    public static String cardNumberToLong(String cardnumber){
        //62x000000000001
        long number = Long.parseLong(cardnumber);
        number = number%1000000000000l;
        return String.format("%012d", number);
    }
    public static String longToCardNumber(String num){
        long x;
        String cardnumber;
        long number = Long.parseLong(num);
        x = ((62 * 1000000000000l) + number) % 7;
        if (x == 4)
            x = 8;
        cardnumber = 62 + (x + String.format("%012d",number));
        return cardnumber;
    }
    public static String getNextCardNumber(String num){
        String next = CardUtil.cardNumberToLong(num);
        next =  String.valueOf(Long.parseLong(next) + 1);
        next = CardUtil.longToCardNumber(next);
        return next;
    }
}
