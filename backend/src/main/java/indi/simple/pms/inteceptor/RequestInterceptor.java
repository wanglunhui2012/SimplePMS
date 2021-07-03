package indi.simple.pms.inteceptor;

import indi.simple.pms.handler.GlobalReturnAndErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:22
 * @Description:
 */
@Slf4j
public class RequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        log.info("---------开始处理请求---------");
        if (obj instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod)obj;
            log.info("---------请求方法:" + method.getBean().getClass().getName() + "." + method.getMethod().getName() + "()---------");
        }

        request.setAttribute("startTime",  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView view) throws Exception {
        log.info("---------结束处理请求---------");
        LocalDateTime start = LocalDateTime.parse((String)request.getAttribute("startTime"),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("---------消耗时间:" + Duration.between(start,LocalDateTime.now()).getSeconds() + "s---------");
    }

    /**
     * 这里的异常如果在{@link GlobalReturnAndErrorHandler}中被处理了，则这里的Exception将为null，未被处理或重新抛出这里才会不为null
     */
    /*public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception ex) throws Exception {
        log.info("---------完成处理请求---------");
        LocalDateTime start = LocalDateTime.parse((String)request.getAttribute("startTime"),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("---------最终耗时:" + Duration.between(start,LocalDateTime.now()).getSeconds() + "---------");
        log.info("---------异常:" + ex + "---------");
    }*/
}

