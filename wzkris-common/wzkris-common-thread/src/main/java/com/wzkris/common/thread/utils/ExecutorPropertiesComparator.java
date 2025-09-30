package com.wzkris.common.thread.utils;

import com.wzkris.common.thread.properties.ExecutorProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 线程池参数比较器
 */
@Slf4j
public class ExecutorPropertiesComparator {

    private final String threadPoolName;

    private boolean hasChanges = false;

    public ExecutorPropertiesComparator(String threadPoolName) {
        this.threadPoolName = threadPoolName;
    }

    public boolean compare(ExecutorProperties local, ExecutorProperties remote) {
        compareField("corePoolSize", local.getCorePoolSize(), remote.getCorePoolSize());
        compareField("maximumPoolSize", local.getMaximumPoolSize(), remote.getMaximumPoolSize());
        compareField("keepAliveTime", local.getKeepAliveTime(), remote.getKeepAliveTime());
        compareField("queueCapacity", local.getQueueCapacity(), remote.getQueueCapacity());
        compareField("threadNamePrefix", local.getThreadNamePrefix(), remote.getThreadNamePrefix());
        compareField("rejectedHandler", local.getRejectedHandlerType(), remote.getRejectedHandlerType());
        compareField("unit", local.getUnit(), remote.getUnit());
        // 其他字段比较...
        return hasChanges;
    }

    private void compareField(String fieldName, Object localValue, Object remoteValue) {
        if (!Objects.equals(localValue, remoteValue)) {
            log.info("线程池'{}' {}发生变化: {} -> {}", threadPoolName,
                    fieldName, localValue, remoteValue);
            hasChanges = true;
        }
    }

}
