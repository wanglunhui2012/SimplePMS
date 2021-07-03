package indi.simple.pms.util;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:40
 * @Description:
 */
public class ValidationUtil {
    public static boolean isEmail(String string) {
        if (string == null) {
            return false;
        } else {
            String regEx = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            return string.matches(regEx);
        }
    }

    public static boolean isUrl(String string) {
        String regEx = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        return string.matches(regEx);
    }
}
