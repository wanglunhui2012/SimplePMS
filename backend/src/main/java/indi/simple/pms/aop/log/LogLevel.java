package indi.simple.pms.aop.log;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:22
 * @Description:
 */
public enum LogLevel {
    TRACE("TRACE"),
    DEBUG("DEBUG"),
    INFO("INFO"),
    WARNING("WARNING"),
    ERROR("ERROR");

    private String value;

    private LogLevel(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}