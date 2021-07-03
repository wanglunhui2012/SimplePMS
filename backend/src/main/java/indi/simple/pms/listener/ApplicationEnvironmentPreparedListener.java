package indi.simple.pms.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:23
 * @Description:
 */
@Component
@Slf4j
public class ApplicationEnvironmentPreparedListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        log.info("---------系统开始启动---------");
    }

}
