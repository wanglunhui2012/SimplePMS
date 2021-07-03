package indi.simple.pms.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:23
 * @Description:
 */
@Component
@Slf4j
public class ApplicationFailedListener implements ApplicationListener<ApplicationFailedEvent> {

    public void onApplicationEvent(ApplicationFailedEvent event) {
        log.info("---------系统启动失败---------");
    }

}