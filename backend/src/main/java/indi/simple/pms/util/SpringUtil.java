package indi.simple.pms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:39
 * @Description:
 */
@Component
public class SpringUtil implements ApplicationContextAware, DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringUtil.class);
    private static ApplicationContext applicationContext = null;

    private SpringUtil() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext != null) {
            LOGGER.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:" + SpringUtil.applicationContext);
        }

        SpringUtil.applicationContext = applicationContext;
    }

    public void destroy() {
        clearHolder();
    }

    private static void assertContextInjected() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder或在SpringBoot启动类中注册SpringContextHolder.");
        }
    }

    public static void clearHolder() {
        LOGGER.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
        applicationContext = null;
    }

    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }

    public static <T> T getBean(String beanName) {
        assertContextInjected();
        return (T) applicationContext.getBean(beanName);
    }

    public static <T> T getBeanUni(String beanName, Class<T> clazz) {
        return applicationContext.getBean(beanName, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        assertContextInjected();
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBeansOfType(Class<T> clazz) {
        assertContextInjected();
        T t = null;
        Map<String, T> map = applicationContext.getBeansOfType(clazz);
        for (Map.Entry<String,T> entry:map.entrySet()){
            t=entry.getValue();
        }

        return t;
    }

    public static boolean containsBean(String beanName) {
        assertContextInjected();
        return applicationContext.containsBean(beanName);
    }

    public static boolean isSingleton(String beanName) {
        return applicationContext.isSingleton(beanName);
    }

    public static Class getType(String beanName) {
        assertContextInjected();
        return applicationContext.getType(beanName);
    }

    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getAliases(name);
    }

    public static <T> T getAopProxy(T invoker) {
        return (T)AopContext.currentProxy();
    }
}
