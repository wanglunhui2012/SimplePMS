package indi.simple.pms.support.file;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author: wanglunhui
 * @Date: 2021/7/2 21:43
 * @Description:
 */
@Component
@Getter
@Setter
public class LocalFileTemplate {
    @Value("${system.file.fileDownloadUrlPrefix}")
    private String fileDownloadUrlPrefix;
    @Value("${system.file.fileUploadPath}")
    private String fileUploadPath;
    @Value("${system.file.defaultAvatarName}")
    private String defaultAvatarName;
    @Value("${system.file.endpoint}")
    private String endpoint;

    @SneakyThrows
    public void upload(String fileName, InputStream inputStream){
        Path path= Paths.get(fileUploadPath);
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }

        Files.copy(inputStream,Paths.get(fileUploadPath+fileName));
    }

    @SneakyThrows
    public void delete(String fileName){
        Files.delete(Paths.get(fileUploadPath+fileName));
    }
}
