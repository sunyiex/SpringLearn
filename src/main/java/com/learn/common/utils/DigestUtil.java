package com.learn.common.utils;

import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;

import java.util.Arrays;

/**
 * Created by Yi on 2015/6/16.
 */
public class DigestUtil {
    private static byte[] bytes;
    private static AesCipherService aesCipherService;

    static {

        String key = "kuaicunbank2015";
        try {
            // MessageDigest sha = MessageDigest.getInstance("SHA-1");

            bytes = key.getBytes("UTF-8");

            // bytes = sha.digest(bytes);
            bytes = Arrays.copyOf(bytes, 16);
        } catch (Exception ex) {
            bytes = null;
        }

        aesCipherService = new AesCipherService();
        aesCipherService.setKeySize(128); // 设置 key 长度 //生成 key
    }

    /**
     * 加密
     * @param value
     * @return
     */
    public static String Encrypt(String value) {

        String encryptText = aesCipherService.encrypt(value.getBytes(), bytes)
                .toHex();
        return encryptText;
    }

    /**
     * 解密
     * @param value
     * @return
     */
    public static String Decrypt(String value) {

        String decryptText = new String(aesCipherService.decrypt(
                Hex.decode(value), bytes).getBytes());
        return decryptText;
    }
}
