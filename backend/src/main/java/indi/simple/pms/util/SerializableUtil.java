package indi.simple.pms.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:39
 * @Description:
 */
public class SerializableUtil {
    public SerializableUtil() {
    }

    public static String serialize(Object object) {
        if (null == object) {
            return null;
        } else {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(object);
                return (new BASE64Encoder()).encodeBuffer(bos.toByteArray());
            } catch (Exception var3) {
                throw new RuntimeException("序列化错误!", var3);
            }
        }
    }

    public static Object deserialize(String objectStr) {
        if (StringUtils.isBlank(objectStr)) {
            return null;
        } else {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream((new BASE64Decoder()).decodeBuffer(objectStr));
                ObjectInputStream ois = new ObjectInputStream(bis);
                return ois.readObject();
            } catch (Exception var3) {
                throw new RuntimeException("反序列化错误!", var3);
            }
        }
    }
}

