package com.wzkris.auth.config;

import cn.hutool.core.util.ArrayUtil;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.exception.BusinessException;
import com.wzkris.common.security.thread.TracingIdRunnable;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 异步配置
 * @date : 2023/12/20 9:25
 */
@EnableAsync(proxyTargetClass = true)
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    /**
     * 异步执行异常处理
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            throwable.printStackTrace();
            StringBuilder sb = new StringBuilder();
            sb.append("Exception message - ").append(throwable.getMessage())
                    .append(", Method name - ").append(method.getName());
            if (ArrayUtil.isNotEmpty(objects)) {
                sb.append(", Parameter value - ").append(Arrays.toString(objects));
            }
            throw new BusinessException(sb.toString());
        };
    }

    @Override
    @Bean
    public Executor getAsyncExecutor() {
        final int corePoolSize = Runtime.getRuntime().availableProcessors();
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("AsyncThreadPool-");
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(3 * corePoolSize);
        executor.setQueueCapacity(10000); // 有界队列长度
        executor.setKeepAliveSeconds(120); // 2min
        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.setThreadFactory(new ThreadFactory() {
            @Override
            public Thread newThread(@NotNull Runnable r) {
                return executor.newThread(new TracingIdRunnable(r, MDC.get(CommonConstants.X_TRACING_ID)));
            }
        });
        executor.setDaemon(true);
        executor.initialize();
        // 使用SpringSecurity的线程池，否则异步线程无法传递用户信息
        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }
}
