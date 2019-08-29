package com.hxy.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * md5加密工具类
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: Md5Util
 * @date 2019年08月26日 15:12:28
 */
public class Md5Util {

    /**
     * 16进制字符
     */
    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 将字节数组转换为16进制字符串
     *
     * @param bytes 字节数组
     * @return 16进制字符串
     */
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuffer resultSb = new StringBuffer();
        for (byte b : bytes) {
            resultSb.append(byteToHexString(b));
        }

        return resultSb.toString();
    }

    /**
     * 将byte数值转换成16进制字符串
     *
     * @param b byte数值
     * @return 16进制字符串
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

    /**
     * 对字符串进行Md5加密
     *
     * @param origin      原始字符串
     * @param charsetname 字符编码
     * @return 加密后的字符串
     */
    private static String md5Encode(String origin, String charsetname) {
        if (null == origin || "".equals(origin.trim())) {
            return null;
        }
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
            }
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    /**
     * 使用utf-8编码对字符串进行md5加密
     *
     * @param origin 原始字符串
     * @return 加密后的字符串
     */
    public static String md5EncodeUtf8(String origin) {
        return md5Encode(origin, "utf-8");
    }

    /**
     * 获取16位数的随机盐
     *
     * @return 随机盐
     */
    public static String getRandomSalt() {
        final Random r = new SecureRandom();
        byte[] salt = new byte[16];
        r.nextBytes(salt);
        return Base64.encodeBase64String(salt);
    }

}
