package com.wzkris.common.thread.refresh;

import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.thread.properties.ExecutorProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * BusinessTpRefresher 测试类
 *
 * @author wzkris
 */
@DisplayName("业务线程池刷新器测试")
class BusinessTpRefresherTest {

    private BusinessTpRefresher refresher;
    private List<ExecutorProperties> executorPropertiesList;
    private DefaultListableBeanFactory beanFactory;
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        executorPropertiesList = new ArrayList<>();
        beanFactory = new DefaultListableBeanFactory();
        applicationContext = mock(ApplicationContext.class);
    }

    @AfterEach
    void tearDown() {
        // 清理
    }

    @Test
    @DisplayName("测试刷新 - 空配置列表")
    void testRefresh_EmptyList() {
        refresher = new BusinessTpRefresher(new ArrayList<>());

        try (MockedStatic<SpringUtil> springUtilMock = Mockito.mockStatic(SpringUtil.class)) {
            springUtilMock.when(SpringUtil::getContext).thenReturn(applicationContext);
            springUtilMock.when(SpringUtil::getFactory).thenReturn(beanFactory);

            refresher.refresh();

            // 验证没有创建任何bean
            assertTrue(beanFactory.getSingletonNames().length == 0);
        }
    }

    @Test
    @DisplayName("测试刷新 - null配置列表")
    void testRefresh_NullList() {
        refresher = new BusinessTpRefresher(null);

        try (MockedStatic<SpringUtil> springUtilMock = Mockito.mockStatic(SpringUtil.class)) {
            springUtilMock.when(SpringUtil::getContext).thenReturn(applicationContext);
            springUtilMock.when(SpringUtil::getFactory).thenReturn(beanFactory);

            refresher.refresh();

            // 验证没有创建任何bean
            assertTrue(beanFactory.getSingletonNames().length == 0);
        }
    }

    @Test
    @DisplayName("测试刷新 - 创建新线程池")
    void testRefresh_CreateNewThreadPool() {
        ExecutorProperties properties = createExecutorProperties("test-pool");
        executorPropertiesList.add(properties);
        refresher = new BusinessTpRefresher(executorPropertiesList);

        try (MockedStatic<SpringUtil> springUtilMock = Mockito.mockStatic(SpringUtil.class)) {
            springUtilMock.when(SpringUtil::getContext).thenReturn(applicationContext);
            springUtilMock.when(SpringUtil::getFactory).thenReturn(beanFactory);
            
            // 模拟bean不存在
            when(applicationContext.getBean("test-pool")).thenThrow(new BeansException("Bean not found") {});

            refresher.refresh();

            // 验证线程池被创建并注册
            assertTrue(beanFactory.containsSingleton("test-pool"));
            Object bean = beanFactory.getSingleton("test-pool");
            assertNotNull(bean);
            assertTrue(bean instanceof ThreadPoolTaskExecutor);
        }
    }

    @Test
    @DisplayName("测试刷新 - 更新现有ThreadPoolTaskExecutor")
    void testRefresh_UpdateThreadPoolTaskExecutor() {
        ExecutorProperties properties = createExecutorProperties("test-pool");
        executorPropertiesList.add(properties);
        refresher = new BusinessTpRefresher(executorPropertiesList);

        // 创建现有的ThreadPoolTaskExecutor
        ThreadPoolTaskExecutor existingExecutor = new ThreadPoolTaskExecutor();
        existingExecutor.setThreadNamePrefix("old-");
        existingExecutor.setCorePoolSize(2);
        existingExecutor.setMaxPoolSize(5);
        existingExecutor.setQueueCapacity(512);
        existingExecutor.initialize();

        try (MockedStatic<SpringUtil> springUtilMock = Mockito.mockStatic(SpringUtil.class)) {
            springUtilMock.when(SpringUtil::getContext).thenReturn(applicationContext);
            springUtilMock.when(SpringUtil::getFactory).thenReturn(beanFactory);
            
            // 注册现有executor
            beanFactory.registerSingleton("test-pool", existingExecutor);
            when(applicationContext.getBean("test-pool")).thenReturn(existingExecutor);

            refresher.refresh();

            // 验证executor被更新
            ThreadPoolTaskExecutor updatedExecutor = (ThreadPoolTaskExecutor) beanFactory.getSingleton("test-pool");
            assertEquals("test-", updatedExecutor.getThreadNamePrefix());
            assertEquals(5, updatedExecutor.getCorePoolSize());
            assertEquals(10, updatedExecutor.getMaxPoolSize());
            assertEquals(1024, updatedExecutor.getQueueCapacity());
        }
    }

    @Test
    @DisplayName("测试刷新 - 更新现有ThreadPoolExecutor")
    void testRefresh_UpdateThreadPoolExecutor() {
        ExecutorProperties properties = createExecutorProperties("test-pool");
        executorPropertiesList.add(properties);
        refresher = new BusinessTpRefresher(executorPropertiesList);

        // 创建现有的ThreadPoolExecutor
        ThreadPoolExecutor existingExecutor = new ThreadPoolExecutor(
                2, 5, 30L, TimeUnit.SECONDS,
                new java.util.concurrent.LinkedBlockingQueue<>(512)
        );

        try (MockedStatic<SpringUtil> springUtilMock = Mockito.mockStatic(SpringUtil.class)) {
            springUtilMock.when(SpringUtil::getContext).thenReturn(applicationContext);
            springUtilMock.when(SpringUtil::getFactory).thenReturn(beanFactory);
            
            // 注册现有executor
            beanFactory.registerSingleton("test-pool", existingExecutor);
            when(applicationContext.getBean("test-pool")).thenReturn(existingExecutor);

            refresher.refresh();

            // 验证新的executor被创建并注册
            assertTrue(beanFactory.containsSingleton("test-pool"));
            Object newExecutor = beanFactory.getSingleton("test-pool");
            assertNotNull(newExecutor);
            assertTrue(newExecutor instanceof ThreadPoolExecutor);
            
            // 验证旧executor被关闭
            assertTrue(existingExecutor.isShutdown());
        }
    }

    @Test
    @DisplayName("测试刷新 - 配置无变化时不更新")
    void testRefresh_NoChanges() {
        ExecutorProperties properties = createExecutorProperties("test-pool");
        executorPropertiesList.add(properties);
        refresher = new BusinessTpRefresher(executorPropertiesList);

        // 创建现有的ThreadPoolTaskExecutor，配置与properties相同
        ThreadPoolTaskExecutor existingExecutor = new ThreadPoolTaskExecutor();
        existingExecutor.setThreadNamePrefix("test-");
        existingExecutor.setCorePoolSize(5);
        existingExecutor.setMaxPoolSize(10);
        existingExecutor.setKeepAliveSeconds(60);
        existingExecutor.setQueueCapacity(1024);
        existingExecutor.initialize();

        try (MockedStatic<SpringUtil> springUtilMock = Mockito.mockStatic(SpringUtil.class)) {
            springUtilMock.when(SpringUtil::getContext).thenReturn(applicationContext);
            springUtilMock.when(SpringUtil::getFactory).thenReturn(beanFactory);
            
            beanFactory.registerSingleton("test-pool", existingExecutor);
            when(applicationContext.getBean("test-pool")).thenReturn(existingExecutor);

            // 第一次刷新
            refresher.refresh();
            
            // 第二次刷新 - 配置相同
            refresher.refresh();

            // 验证executor仍然是同一个实例（没有被替换）
            assertSame(existingExecutor, beanFactory.getSingleton("test-pool"));
        }
    }

    @Test
    @DisplayName("测试刷新 - 多个线程池")
    void testRefresh_MultipleThreadPools() {
        ExecutorProperties properties1 = createExecutorProperties("pool1");
        ExecutorProperties properties2 = createExecutorProperties("pool2");
        properties2.setThreadPoolName("pool2");
        executorPropertiesList.add(properties1);
        executorPropertiesList.add(properties2);
        refresher = new BusinessTpRefresher(executorPropertiesList);

        try (MockedStatic<SpringUtil> springUtilMock = Mockito.mockStatic(SpringUtil.class)) {
            springUtilMock.when(SpringUtil::getContext).thenReturn(applicationContext);
            springUtilMock.when(SpringUtil::getFactory).thenReturn(beanFactory);
            
            when(applicationContext.getBean(anyString())).thenThrow(new BeansException("Bean not found") {});

            refresher.refresh();

            // 验证两个线程池都被创建
            assertTrue(beanFactory.containsSingleton("pool1"));
            assertTrue(beanFactory.containsSingleton("pool2"));
        }
    }

    @Test
    @DisplayName("测试刷新 - 不同拒绝策略")
    void testRefresh_DifferentRejectedHandlers() {
        ExecutorProperties properties = createExecutorProperties("test-pool");
        properties.setRejectedHandlerType("CallerRunsPolicy");
        executorPropertiesList.add(properties);
        refresher = new BusinessTpRefresher(executorPropertiesList);

        try (MockedStatic<SpringUtil> springUtilMock = Mockito.mockStatic(SpringUtil.class)) {
            springUtilMock.when(SpringUtil::getContext).thenReturn(applicationContext);
            springUtilMock.when(SpringUtil::getFactory).thenReturn(beanFactory);
            
            when(applicationContext.getBean("test-pool")).thenThrow(new BeansException("Bean not found") {});

            refresher.refresh();

            // 验证线程池被创建
            assertTrue(beanFactory.containsSingleton("test-pool"));
        }
    }

    // 辅助方法
    private ExecutorProperties createExecutorProperties(String threadPoolName) {
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

