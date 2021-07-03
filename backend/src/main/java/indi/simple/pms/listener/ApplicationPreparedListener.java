package indi.simple.pms.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:23
 * @Description:
 */
@Component
@Slf4j
public class ApplicationPreparedListener implements ApplicationListener<ApplicationPreparedEvent> {

    public void onApplicationEvent(ApplicationPreparedEvent event) {
        log.info("---------系统正在启动---------");
    }

}
