package com.wzkris.common.thread.utils;

import com.wzkris.common.thread.properties.ExecutorProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 业务线程池参数比较器
 *
 * @author wzkris
 * @date 2025/12/02
 */
@Slf4j
public class BusinessExecutorPropertiesComparator {

    private final String threadPoolName;

    private boolean hasChanges = false;

    public BusinessExecutorPropertiesComparator(String threadPoolName) {
        this.threadPoolName = threadPoolName;
    }

    public boolean compare(ExecutorProperties local, ExecutorProperties remote) {
        hasChanges = false;
        compareField("corePoolSize", local.getCorePoolSize(), remote.getCorePoolSize());
        compareField("maximumPoolSize", local.getMaximumPoolSize(), remote.getMaximumPoolSize());
        compareField("keepAliveTime", local.getKeepAliveTime(), remote.getKeepAliveTime());
        compareField("queueCapacity", local.getQueueCapacity(), remote.getQueueCapacity());
        compareField("threadNamePrefix", local.getThreadNamePrefix(), remote.getThreadNamePrefix());
        compareField("rejectedHandler", local.getRejectedHandlerType(), remote.getRejectedHandlerType());
        compareField("unit", local.getUnit(), remote.getUnit());
        return hasChanges;
    }

    private void compareField(String fieldName, Object localValue, Object remoteValue) {
        if (!Objects.equals(localValue, remoteValue)) {
            log.info("线程池'{}' {}发生变化: {} -> {}", threadPoolName, fieldName, localValue, remoteValue);
            hasChanges = true;
        }
    }

}

