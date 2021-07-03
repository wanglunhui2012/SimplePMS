package indi.simple.pms.listener;

import indi.simple.pms.support.file.LocalFileTemplate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:23
 * @Description:
 */
@Component
@Slf4j
public class ApplicationStartedListener implements ApplicationListener<ApplicationStartedEvent> {
    @Autowired
    private LocalFileTemplate localFileTemplate;

    @Override
    @SneakyThrows
    public void onApplicationEvent(ApplicationStartedEvent event) {
        // 启动后将默认图片放在合适目录
        Path path=Paths.get(localFileTemplate.getFileUploadPath()+localFileTemplate.getDefaultAvatarName());
        if(!Files.exists(path)){
            Files.createDirectories(path.getParent());
            InputStream inputStream=new ClassPathResource("static/defaultAvatar.png").getInputStream(); // 类路径下的输入流
            Files.copy(inputStream,path);
        }

        log.info("---------系统启动完成---------");
    }
}

