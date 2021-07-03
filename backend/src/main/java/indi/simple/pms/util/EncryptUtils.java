package indi.simple.pms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:35
 * @Description:
 */
public class EncryptUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptUtils.class);
    private static String strKey = "Passw0rd";
    private static String strParam = "Passw0rd";
    private static final String[] hexDigits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public EncryptUtils() {
    }

    public static String desEncrypt(String source) throws Exception {
        if (source != null && source.length() != 0) {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(strKey.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(strParam.getBytes("UTF-8"));
            cipher.init(1, secretKey, iv);
            return byte2hex(cipher.doFinal(source.getBytes("UTF-8"))).toUpperCase();
        } else {
            return null;
        }
    }

    public static String byte2hex(byte[] inStr) {
        StringBuffer out = new StringBuffer(inStr.length * 2);

        for(int n = 0; n < inStr.length; ++n) {
            String stmp = Integer.toHexString(inStr[n] & 255);
            if (stmp.length() == 1) {
                out.append("0" + stmp);
            } else {
                out.append(stmp);
            }
        }

        return out.toString();
    }

    public static byte[] hex2byte(byte[] b) {
        if (b.length % 2 != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        } else {
            byte[] b2 = new byte[b.length / 2];

            for(int n = 0; n < b.length; n += 2) {
                String item = new String(b, n, 2);
                b2[n / 2] = (byte)Integer.parseInt(item, 16);
            }

            return b2;
        }
    }

    public static String desDecrypt(String source) throws Exception {
        if (source != null && source.length() != 0) {
            byte[] src = hex2byte(source.getBytes());
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(strKey.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(strParam.getBytes("UTF-8"));
            cipher.init(2, secretKey, iv);
            byte[] retByte = cipher.doFinal(src);
            return new String(retByte);
        } else {
            return null;
        }
    }

    public static String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

    public static String encryptBASE64(String code) {
        try {
            return (new BASE64Encoder()).encodeBuffer(code.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException var2) {
            LOGGER.error(ThrowableUtil.getStackTrace(var2));
            return null;
        }
    }

    public static byte[] decryptBASE64(String code) {
        try {
            return (new BASE64Decoder()).decodeBuffer(code);
        } catch (IOException var2) {
            LOGGER.error(ThrowableUtil.getStackTrace(var2));
            return null;
        }
    }

    public static byte[] encryptMD5(String code, String charsetName) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(code.getBytes(charsetName));
            return md5.digest();
        } catch (NoSuchAlgorithmException var3) {
            LOGGER.error(ThrowableUtil.getStackTrace(var3));
        } catch (UnsupportedEncodingException var4) {
            LOGGER.error(ThrowableUtil.getStackTrace(var4));
        }

        return null;
    }

    public static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        byte[] var2 = b;
        int var3 = b.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte aB = var2[var4];
            resultSb.append(byteToHexString(aB));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (b < 0) {
            n = 256 + b;
        }

        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static byte[] encryptSHA256(String code) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA");
            sha.update(code.getBytes("UTF-8"));
            return sha.digest();
        } catch (NoSuchAlgorithmException var2) {
            LOGGER.error(ThrowableUtil.getStackTrace(var2));
        } catch (UnsupportedEncodingException var3) {
            LOGGER.error(ThrowableUtil.getStackTrace(var3));
        }

        return null;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(encryptPassword("e10adc3949ba59abbe56e057f20f883e"));
        System.out.println(encryptBASE64("123456"));
        System.out.println(new String(decryptBASE64("MTIzNDU2"), "UTF-8"));
        System.out.println(byteArrayToHexString(encryptMD5("123456", "UTF-8")));
        System.out.println(byteArrayToHexString(encryptSHA256("123456")));
        System.out.println((new BCryptPasswordEncoder()).encode("123456"));
        System.out.println((new BCryptPasswordEncoder()).encode("e10adc3949ba59abbe56e057f20f883eYzcmCZNvbXocrsz9dm8e"));
        System.out.println((new BCryptPasswordEncoder()).matches("123456", "$2a$10$ue7RFxT/IK7VHuRSm5woAehjy3/rIdfV5ARSLj17MEZEnlpHIVTEa"));
        System.out.println(byteArrayToHexString(encryptMD5("123456", "UTF-8")));
        System.out.println(byteArrayToHexString(encryptMD5("e10adc3949ba59abbe56e057f20f883eYzcmCZNvbXocrsz9dm8e", "UTF-8")));
    }
}

