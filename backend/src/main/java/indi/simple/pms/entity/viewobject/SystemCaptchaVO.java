package indi.simple.pms.entity.viewobject;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:20
 * @Description:
 */
public class SystemCaptchaVO {
    private String uuid;
    private String captcha;

    public SystemCaptchaVO(String uuid, String captcha) {
        this.uuid = uuid;
        this.captcha = captcha;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCaptcha() {
        return this.captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
