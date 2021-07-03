package indi.simple.pms.aop.log;

import indi.simple.pms.service.AsyncService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 21:21
 * @Description:
 */
@Component
@Aspect
public class LogAspect {
    private InheritableThreadLocal<LocalDateTime> localDateTimeThreadLocal=new InheritableThreadLocal<>();
    @Autowired
    private AsyncService asyncService;

    public LogAspect() {
    }

    @Pointcut("@annotation(indi.simple.pms.aop.log.Log)")
    public void logPointcut() {
    }

    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 开启Request线程共享，否则子线程获取不到Request
        ServletRequestAttributes att = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(att, true);
        this.localDateTimeThreadLocal.set(LocalDateTime.now());
        Object result = joinPoint.proceed();
        asyncService.saveSysLog(joinPoint, LogLevel.INFO, null,localDateTimeThreadLocal);
        this.localDateTimeThreadLocal.remove();
        return result;
    }

    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        asyncService.saveSysLog(joinPoint, LogLevel.ERROR, e,localDateTimeThreadLocal);
        this.localDateTimeThreadLocal.remove();
    }
}