package com.wzkris.common.thread.utils;

import com.wzkris.common.thread.properties.ExecutorProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BusinessExecutorPropertiesComparator 测试类
 *
 * @author wzkris
 */
@DisplayName("业务线程池参数比较器测试")
class BusinessExecutorPropertiesComparatorTest {

    @Test
    @DisplayName("测试比较 - 完全相同")
    void testCompare_Identical() {
        String threadPoolName = "test-pool";
        BusinessExecutorPropertiesComparator comparator = new BusinessExecutorPropertiesComparator(threadPoolName);
        
        ExecutorProperties local = createProperties(threadPoolName);
        ExecutorProperties remote = createProperties(threadPoolName);
        
        assertFalse(comparator.compare(local, remote));
    }

    @Test
    @DisplayName("测试比较 - corePoolSize不同")
    void testCompare_DifferentCorePoolSize() {
        String threadPoolName = "test-pool";
        BusinessExecutorPropertiesComparator comparator = new BusinessExecutorPropertiesComparator(threadPoolName);
        
        ExecutorProperties local = createProperties(threadPoolName);
        ExecutorProperties remote = createProperties(threadPoolName);
        remote.setCorePoolSize(10);
        
        assertTrue(comparator.compare(local, remote));
    }

    @Test
    @DisplayName("测试比较 - maximumPoolSize不同")
    void testCompare_DifferentMaximumPoolSize() {
        String threadPoolName = "test-pool";
        BusinessExecutorPropertiesComparator comparator = new BusinessExecutorPropertiesComparator(threadPoolName);
        
        ExecutorProperties local = createProperties(threadPoolName);
        ExecutorProperties remote = createProperties(threadPoolName);
        remote.setMaximumPoolSize(20);
        
        assertTrue(comparator.compare(local, remote));
    }

    @Test
    @DisplayName("测试比较 - keepAliveTime不同")
    void testCompare_DifferentKeepAliveTime() {
        String threadPoolName = "test-pool";
        BusinessExecutorPropertiesComparator comparator = new BusinessExecutorPropertiesComparator(threadPoolName);
        
        ExecutorProperties local = createProperties(threadPoolName);
        ExecutorProperties remote = createProperties(threadPoolName);
        remote.setKeepAliveTime(120L);
        
        assertTrue(comparator.compare(local, remote));
    }

    @Test
    @DisplayName("测试比较 - queueCapacity不同")
    void testCompare_DifferentQueueCapacity() {
        String threadPoolName = "test-pool";
        BusinessExecutorPropertiesComparator comparator = new BusinessExecutorPropertiesComparator(threadPoolName);
        
        ExecutorProperties local = createProperties(threadPoolName);
        ExecutorProperties remote = createProperties(threadPoolName);
        remote.setQueueCapacity(2048);
        
        assertTrue(comparator.compare(local, remote));
    }

    @Test
    @DisplayName("测试比较 - threadNamePrefix不同")
    void testCompare_DifferentThreadNamePrefix() {
        String threadPoolName = "test-pool";
        BusinessExecutorPropertiesComparator comparator = new BusinessExecutorPropertiesComparator(threadPoolName);
        
        ExecutorProperties local = createProperties(threadPoolName);
        ExecutorProperties remote = createProperties(threadPoolName);
        remote.setThreadNamePrefix("new-prefix-");
        
        assertTrue(comparator.compare(local, remote));
    }

    @Test
    @DisplayName("测试比较 - rejectedHandlerType不同")
    void testCompare_DifferentRejectedHandlerType() {
        String threadPoolName = "test-pool";
        BusinessExecutorPropertiesComparator comparator = new BusinessExecutorPropertiesComparator(threadPoolName);
        
        ExecutorProperties local = createProperties(threadPoolName);
        ExecutorProperties remote = createProperties(threadPoolName);
        remote.setRejectedHandlerType("CallerRunsPolicy");
        
        assertTrue(comparator.compare(local, remote));
    }

    @Test
    @DisplayName("测试比较 - unit不同")
    void testCompare_DifferentUnit() {
        String threadPoolName = "test-pool";
        BusinessExecutorPropertiesComparator comparator = new BusinessExecutorPropertiesComparator(threadPoolName);
        
        ExecutorProperties local = createProperties(threadPoolName);
        ExecutorProperties remote = createProperties(threadPoolName);
        remote.setUnit(TimeUnit.MINUTES);
        
        assertTrue(comparator.compare(local, remote));
    }

    @Test
    @DisplayName("测试比较 - 多个字段不同")
    void testCompare_MultipleDifferences() {
        String threadPoolName = "test-pool";
        BusinessExecutorPropertiesComparator comparator = new BusinessExecutorPropertiesComparator(threadPoolName);
        
        ExecutorProperties local = createProperties(threadPoolName);
        ExecutorProperties remote = createProperties(threadPoolName);
        remote.setCorePoolSize(10);
        remote.setMaximumPoolSize(20);
        remote.setQueueCapacity(2048);
        
        assertTrue(comparator.compare(local, remote));
    }

    @Test
    @DisplayName("测试比较 - null值处理")
    void testCompare_WithNullValues() {
        String threadPoolName = "test-pool";
        BusinessExecutorPropertiesComparator comparator = new BusinessExecutorPropertiesComparator(threadPoolName);
        
        ExecutorProperties local = new ExecutorProperties();
        local.setThreadPoolName(threadPoolName);
        
        ExecutorProperties remote = new ExecutorProperties();
        remote.setThreadPoolName(threadPoolName);
        remote.setCorePoolSize(5);
        
        assertTrue(comparator.compare(local, remote));
    }

    @Test
    @DisplayName("测试多次比较")
    void testCompare_MultipleCalls() {
        String threadPoolName = "test-pool";
        BusinessExecutorPropertiesComparator comparator = new BusinessExecutorPropertiesComparator(threadPoolName);
        
        ExecutorProperties local = createProperties(threadPoolName);
        ExecutorProperties remote1 = createProperties(threadPoolName);
        ExecutorProperties remote2 = createProperties(threadPoolName);
        remote2.setCorePoolSize(10);
        
        // 第一次比较 - 相同
        assertFalse(comparator.compare(local, remote1));
        
        // 第二次比较 - 不同
        assertTrue(comparator.compare(local, remote2));
        
        // 第三次比较 - 相同
        assertFalse(comparator.compare(local, remote1));
    }

    // 辅助方法
    private ExecutorProperties createProperties(String threadPoolName) {
        ExecutorProperties properties = new ExecutorProperties();
        properties.setThreadPoolName(threadPoolName);
        properties.setThreadNamePrefix("test-");
        properties.setCorePoolSize(5);
        properties.setMaximumPoolSize(10);
        properties.setKeepAliveTime(60L);
        properties.setUnit(TimeUnit.SECONDS);
        properties.setQueueCapacity(1024);
        properties.setRejectedHandlerType("AbortPolicy");
        return properties;
    }
}

