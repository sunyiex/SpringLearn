package com.learn;

import com.learn.common.utils.CardUtil;

/**
 * Created by Yi on 2015/6/17.
 */
public class cardTest {
    public static void main(String[] args) {
        String num = CardUtil.cardNumberToLong("625123450000012");
        System.out.println(num);
       num =  String.valueOf(Long.parseLong(num) + 1);
        num = CardUtil.longToCardNumber(num);
        System.out.println(num);

        System.out.println(CardUtil.getNextCardNumber("625123450000012"));
    }
}
