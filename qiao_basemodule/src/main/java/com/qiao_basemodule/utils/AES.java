package com.qiao_basemodule.utils;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

public class AES {

    public static String ALGORITHN = "AES";
    public static byte[] encrypt () {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHN);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


}
