package indi.simple.pms.constant;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 21:36
 * @Description:
 */
public class SystemRedisConstant {
    public static final String SPLIT="::";

    public static final String LOGIN_CAPTCHA="login"+SPLIT+"captcha";
    public static final String LOGIN_JWTUSER = "login"+SPLIT+"jwtuser";
    public static final String ONLINE_USER="online"+SPLIT+"user";


    public static final String SYSTEM_PERMISSION_PAGE = "system:permission:page";
    public static final String SYSTEM_LOG_PAGE = "system:log:page";
    public static final String SYSTEM_USER_PAGE = "system:user:page";
    public static final String SYSTEM_ROLE_PAGE = "system:role:page";
    public static final String SYSTEM_MENU_PAGE = "system:menu:page";
    public static final String SYSTEM_JOB_PAGE = "system:job:page";
    public static final String SYSTEM_DICTIONARY_DETAIL_PAGE = "system:dictionary_detail:page";
    public static final String SYSTEM_DICTIONARY_PAGE = "system:dictionary:page";
    public static final String SYSTEM_DEPARTMENT_PAGE = "system::department::";
    public static final String SYSTEM_QUARTZ_LOG_PAGE = "system:quartz:log:page";
    public static final String SYSTEM_LIMIT_IP = "system:limit:ip:";
    public static final String SYSTEM_LIMIT_METHOD = "system:limit:ip:";
    public static final String SYSTEM_DAILY_INCREMENT_ID = "system:daily:increment:id";

}