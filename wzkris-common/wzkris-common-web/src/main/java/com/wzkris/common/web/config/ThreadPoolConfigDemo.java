package com.wzkris.common.web.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置模板
 *
 * @author wzkris
 **/
public class ThreadPoolConfigDemo {
    /**
     * 核心线程数量
     */
    private static final int corePoolSize = Runtime.getRuntime().availableProcessors();

    @Primary
    @Bean(name = "threadPoolExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize + 3);
        executor.setMaxPoolSize(3 * corePoolSize);
        executor.setQueueCapacity(1000);
        executor.setKeepAliveSeconds(300);
        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * 执行周期性或定时任务
     */
    @Bean(name = "scheduledExecutor")
    protected ScheduledExecutorService scheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(corePoolSize,
                ThreadFactoryBuilder.create().setNamePrefix("schedule-pool-%d").setDaemon(true).build(),
                new ThreadPoolExecutor.CallerRunsPolicy()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
//                log.error("定时线程池发生异常，errmsg：{}", t.getMessage(), t);
            }
        };
    }
}
