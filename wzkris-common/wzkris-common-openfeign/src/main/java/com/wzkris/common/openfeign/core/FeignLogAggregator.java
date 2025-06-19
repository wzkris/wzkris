package com.wzkris.common.openfeign.core;

import com.wzkris.common.core.threads.TracingIdRunnable;
import com.wzkris.common.core.utils.StringUtil;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 日志缓存管理器
 *
 * @author wzkris
 */
public enum FeignLogAggregator {

    INSTANCE;

    private final ConcurrentHashMap<String, LogCounter> logCache = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(
            1,
            new ThreadFactory() {
                private final AtomicInteger threadNumber = new AtomicInteger(1);

                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(new TracingIdRunnable(r), "feign-log-Aggregator-Thread-" + threadNumber.getAndIncrement());
                    t.setDaemon(true);
                    return t;
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    static {
        INSTANCE.scheduler.scheduleAtFixedRate(
                INSTANCE::flushLogs,
                1, 1, TimeUnit.SECONDS);
    }

    void count(String log) {
        logCache.compute(log, (k, v) ->
                v == null ? new LogCounter() : v.increment());
    }

    void flushLogs() {
        logCache.forEach((key, counter) -> {
            LoggerFactory.getLogger(key.split(StringUtil.HASH)[0])
                    .error("[聚合日志] 首次触发时间: {} 触发次数: {},\n " +
                            "{}", counter.getFirstOccurrenceTime(), counter.getCount(), key);
        });
        logCache.clear();
    }

    /**
     * 计数包装类
     */
    static class LogCounter {

        private final AtomicInteger count = new AtomicInteger(1);

        private final Date firstOccurrenceTime = new Date();

        LogCounter increment() {
            count.incrementAndGet();
            return this;
        }

        int getCount() {
            return count.get();
        }

        String getFirstOccurrenceTime() {
            return DateFormat.getDateTimeInstance().format(firstOccurrenceTime);
        }

    }
}
