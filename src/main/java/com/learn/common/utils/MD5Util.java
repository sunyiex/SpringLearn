package com.learn.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Yi on 2015/6/1.
 */
public class MD5Util {
    public static final String MD5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4',
                '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
//    public static final String MD52(String str, String charSet) {
//        MessageDigest messageDigest = null;
//        try {
//            messageDigest = MessageDigest.getInstance("MD5");
//            messageDigest.reset();
//            if (charSet == null) {
//                messageDigest.update(str.getBytes());
//            } else {
//                messageDigest.update(str.getBytes(charSet));
//            }
//        } catch (NoSuchAlgorithmException e) {
//        } catch (UnsupportedEncodingException e) {
//        }
//
//        byte[] byteArray = messageDigest.digest();
//        StringBuffer md5StrBuff = new StringBuffer();
//        for (int i = 0; i < byteArray.length; i++) {
//            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
//                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
//            else
//                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
//        }
//        return md5StrBuff.toString();
//    }
//
//    public static final String MD52(String str) throws NoSuchAlgorithmException {
//        MessageDigest messageDigest = null;
//        messageDigest = MessageDigest.getInstance("MD5");
//        messageDigest.reset();
//        messageDigest.update(str.getBytes());
//
//
//        byte[] byteArray = messageDigest.digest();
//        StringBuffer md5StrBuff = new StringBuffer();
//        for (int i = 0; i < byteArray.length; i++) {
//            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
//                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
//            else
//                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
//        }
//        return md5StrBuff.toString();
//    }
}
