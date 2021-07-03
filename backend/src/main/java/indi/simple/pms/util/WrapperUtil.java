package indi.simple.pms.util;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:40
 * @Description:
 */
public class WrapperUtil<T> {
    public T value;

    public WrapperUtil() {
    }

    public static <T> WrapperUtil newInstance() {
        return new WrapperUtil();
    }
}
