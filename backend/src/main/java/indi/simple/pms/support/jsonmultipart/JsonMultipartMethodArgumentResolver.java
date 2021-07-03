package indi.simple.pms.support.jsonmultipart;

import indi.simple.pms.entity.datatransferobject.JsonMultipartDTO;
import indi.simple.pms.util.JsonUtil;
import org.springframework.core.MethodParameter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:26
 * @Description:
 */
public class JsonMultipartMethodArgumentResolver implements HandlerMethodArgumentResolver {
    public JsonMultipartMethodArgumentResolver() {
    }

    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(JsonMultipart.class);
    }

    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        JsonMultipart jsonMultipart = methodParameter.getParameterAnnotation(JsonMultipart.class);
        Class clz = methodParameter.getParameterType();
        Class<?> classInfo = jsonMultipart.classInfo();
        HttpServletRequest servletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        Map<String, String[]> parameterMap = servletRequest.getParameterMap();
        MultiValueMap<String, MultipartFile> multipartFileMultiValueMap = ((StandardMultipartHttpServletRequest)servletRequest).getMultiFileMap();
        JsonMultipartDTO jsonMultipartDTO = (JsonMultipartDTO)clz.newInstance();
        jsonMultipartDTO.setJson(JsonUtil.jsonToObject(parameterMap.get("json")[0], classInfo));
        jsonMultipartDTO.setMultipartFileList(multipartFileMultiValueMap.get("multipartFileList"));
        return jsonMultipartDTO;
    }
}
