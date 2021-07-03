package indi.simple.pms.entity.datatransferobject;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:18
 * @Description:
 */
@Getter
@Setter
public class JsonMultipartDTO<T> {

    private T json;

    private List<MultipartFile> multipartFileList;

}