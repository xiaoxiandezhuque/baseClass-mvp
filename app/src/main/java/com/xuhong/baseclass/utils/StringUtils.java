package com.xuhong.baseclass.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by xuhong on 2016/8/4.
 */

public class StringUtils {

    /**
     * 将字符串转成MD5值
     *
     * @param string
     * @return
     */
    public static String stringToMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }


    public static void isEmptyException(String input,String hint) throws StringIsEmptyException {
        if (input == null || "".equals(input) || "null".equals(input)){
            throw  new StringIsEmptyException(hint);
        }

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
               return;
            }
        }
        throw  new StringIsEmptyException(hint);
    }


    public static boolean isEmpty(String input)  {
        if (input == null || "".equals(input) || "null".equals(input)){
           return true;
        }

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static class StringIsEmptyException extends Exception{
        public StringIsEmptyException(String hint){
            super(hint);
        }
    }
}
