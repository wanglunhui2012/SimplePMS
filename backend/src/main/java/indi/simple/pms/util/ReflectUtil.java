package indi.simple.pms.util;

import com.baomidou.mybatisplus.annotation.TableName;
import indi.simple.pms.entity.dataobject.SystemUserDO;

import java.lang.annotation.Annotation;

/**
 * @Author: wanglunhui
 * @Date: 2021/5/16 9:20
 * @Description:
 */
public class ReflectUtil {
    /**
     * 获取类上的注解
     */
    public static <T extends Annotation> T getClassAnnotation(Class<?> targetClass,Class<T> targetAnnotation){
        T annotation=targetClass.getAnnotation(targetAnnotation);
        if (annotation==null){
            throw new RuntimeException(targetClass.getSimpleName()+"类不存在"+targetAnnotation.getSimpleName()+"注解!");
        }

        return annotation;
    }

    public static void main(String[] args) {
        System.out.println(ReflectUtil.getClassAnnotation(SystemUserDO.class, TableName.class).value());
    }
}
