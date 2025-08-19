package com.wzkris.common.thread.properties;

import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
public class ExecutorProperties {

    /**
     * Name of ThreadPool.
     */
    private String threadPoolName;

    /**
     * Thread name prefix.
     */
    private String threadNamePrefix = "";

    /**
     * CoreSize of ThreadPool.
     */
    private int corePoolSize = 1;

    /**
     * MaxSize of ThreadPool.
     */
    private int maximumPoolSize = Runtime.getRuntime().availableProcessors();

    /**
     * When the number of threads is greater than the core,
     * this is the maximum time that excess idle threads
     * will wait for new tasks before terminating.
     */
    private long keepAliveTime = 60;

    /**
     * Timeout unit.
     */
    private TimeUnit unit = TimeUnit.SECONDS;

    /**
     * BlockingQueue capacity.
     */
    private int queueCapacity = 1024;

    /**
     * 拒绝策略
     */
    private String rejectedHandlerType = "AbortPolicy";

}
