package indi.simple.pms.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:36
 * @Description:
 */
public class I18INUtil {
    public static String message(String code, Object... args) {
        MessageSource messageSource = (MessageSource)SpringUtil.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}

