package indi.simple.pms.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:32
 * @Description:
 */
@EnableAsync
@Slf4j
@Configuration
public class ThreadPoolConfig implements AsyncConfigurer {
    private int corePoolSize = 8;
    private int maxPoolSize = 16;
    private int queueCapacity = 20;
    private int keepAliveSeconds = 60;
    private int awaitTerminationSeconds = 60;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(this.corePoolSize);
        executor.setMaxPoolSize(this.maxPoolSize);
        executor.setQueueCapacity(this.queueCapacity);
        executor.setKeepAliveSeconds(this.keepAliveSeconds);
        executor.setThreadNamePrefix("SimplePMS-ThreadPoolTaskExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        executor.setThreadFactory(new MyTheadFactory());
        executor.initialize();

        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            log.error("exception method:" + method.getName() + throwable.getMessage());
        };
    }
}
