package indi.simple.pms.service;

import cn.hutool.json.JSONObject;
import indi.simple.pms.aop.log.Log;
import indi.simple.pms.aop.log.LogLevel;
import indi.simple.pms.entity.dataobject.SystemLogDO;
import indi.simple.pms.util.JsonUtil;
import indi.simple.pms.util.RequestUtil;
import indi.simple.pms.util.SecurityUtil;
import indi.simple.pms.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @Author: wanglunhui
 * @Date: 2021/6/27 23:18
 * @Description:
 */
@Service
@Slf4j
public class AsyncService {
    private static final Logger LOGGER= LoggerFactory.getLogger(AsyncService.class);
    @Autowired
    private SystemLogService systemLogService;

    @Async
    public void saveSysLog(JoinPoint joinPoint, LogLevel logLevel, Throwable e, ThreadLocal<LocalDateTime> localDateTimeThreadLocal) {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        HttpServletRequest request = RequestUtil.getHttpServletRequest();
        SystemLogDO systemLogDO = new SystemLogDO();
        Log log = method.getAnnotation(Log.class);
        systemLogDO.setName(log.value());
        StringBuilder params = new StringBuilder("{");
        Object[] argValues = joinPoint.getArgs();
        String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
        if (argValues != null) {
            for(int i = 0; i < argValues.length; ++i) {
                if (!StringUtil.equalsAny(argNames[i], "request", "response")) {
                    if (argValues[i] instanceof MultipartFile) {
                        params.append(" ").append(argNames[i]).append(": ").append(((MultipartFile)argValues[i]).getOriginalFilename());
                    } else {
                        params.append(" ").append(argNames[i]).append(": ").append(JsonUtil.objectToJson(argValues[i]));
                    }
                }
            }
        }

        systemLogDO.setParams(params.toString() + "}");
        Optional<String> username = Optional.empty();
        if (log.isLogin()) {
            try {
                JSONObject jsonObject = new JSONObject(joinPoint.getArgs()[0]);
                username = Optional.ofNullable((String)jsonObject.get("username"));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else {
            username = Optional.of(SecurityUtil.getJwtUserBO().getUsername());
        }

        systemLogDO.setUsername(username.orElse("无"));
        systemLogDO.setUrl(request.getRequestURI());
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        systemLogDO.setMethod(className + "." + methodName + "()");
        systemLogDO.setTime(Duration.between(localDateTimeThreadLocal.get(), LocalDateTime.now()).toMillis());
        String ip = RequestUtil.getIP(request);
        systemLogDO.setIp(ip);
        systemLogDO.setLogType(log.isLogin()?"0":"1");
        systemLogDO.setException(e != null ? e.getMessage() : null);
        systemLogDO.setCreateTime(localDateTimeThreadLocal.get());
        if (log.isLogin()) {
            LOGGER.info("用户登录：{}", JsonUtil.objectToJson(systemLogDO));
        }

        this.systemLogService.save(systemLogDO);

    }
}
