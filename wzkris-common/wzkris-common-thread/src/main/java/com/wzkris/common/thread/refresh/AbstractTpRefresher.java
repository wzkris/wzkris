/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wzkris.common.thread.refresh;

import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.thread.properties.ExecutorProperties;
import com.wzkris.common.thread.properties.TpProperties;
import com.wzkris.common.thread.refresh.adapter.WebServerTpAdapter;
import com.wzkris.common.thread.utils.ExecutorPropertiesComparator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract class for refreshing properties in a Spring environment.
 *
 * @author wzkris
 * @date 2025/8/8
 */
@Slf4j
public abstract class AbstractTpRefresher implements ApplicationRunner {

    protected final TpProperties tpProperties;

    private final WebServerTpAdapter webServerTpAdapter;

    private TpProperties localTpProperties;// 本地的变量，用来对比

    protected AbstractTpRefresher(TpProperties tpProperties, WebServerTpAdapter webServerTpAdapter) {
        this.tpProperties = tpProperties;
        this.webServerTpAdapter = webServerTpAdapter;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        refresh();
    }

    /**
     * 刷新配置
     */
    protected final void refresh() {
        boolean hasChanges = false;

        // 1. 刷新Web服务器线程池
        hasChanges |= refreshWebServerThreadPool();

        // 2. 刷新业务线程池
        hasChanges |= refreshBusinessThreadPools();

        // 3. 更新本地配置
        if (hasChanges) {
            updateLocalConfiguration();
        }
    }

    private boolean refreshBusinessThreadPools() {
        boolean hasAnyChange = false;
        // 刷新线程池配置
        for (ExecutorProperties properties : tpProperties.getTpExecutors()) {
            try {
                Object bean = SpringUtil.getContext().getBean(properties.getThreadPoolName());

                // 查找对应的本地配置
                ExecutorProperties localProp = findLocalExecutorProperties(properties.getThreadPoolName());
                if (localProp == null) {
                    log.info("线程池'{}'初次配置更新", properties.getThreadPoolName());
                    applyConfiguration(bean, properties);
                    hasAnyChange = true;
                    continue;
                }

                // 比较配置变化
                if (new ExecutorPropertiesComparator(properties.getThreadPoolName()).compare(localProp, properties)) {
                    log.info("线程池'{}'进行配置更新", properties.getThreadPoolName());
                    applyConfiguration(bean, properties);
                    hasAnyChange = true;
                }
            } catch (BeansException e) {
                applyConfiguration(null, properties);
                log.info("线程池'{}'成功创建并注册", properties.getThreadPoolName());
                hasAnyChange = true;
            }
        }
        return hasAnyChange;
    }

    private void updateLocalConfiguration() {
        if (localTpProperties == null) {
            localTpProperties = new TpProperties();
        }
        localTpProperties.setTomcatExecutor(tpProperties.getTomcatExecutor());
        localTpProperties.setTpExecutors(tpProperties.getTpExecutors());
    }

    /**
     * 刷新Web服务器线程池配置
     *
     * @return 是否刷新了配置
     */
    private boolean refreshWebServerThreadPool() {
        ExecutorProperties webExecutorProperties = tpProperties.getTomcatExecutor();
        if (webExecutorProperties == null) {
            return false;
        }

        // 查找本地Web服务器配置
        ExecutorProperties localWebProp = localTpProperties != null ? localTpProperties.getTomcatExecutor() : null;

        if (localWebProp == null) {
            // 初始化Web服务器配置
            log.info("Web服务器线程池进行启动配置更新");
            webServerTpAdapter.refreshWeb(webExecutorProperties);
            return true;
        }

        // 比较Web服务器配置变化
        boolean hasWebChanges = new ExecutorPropertiesComparator(webExecutorProperties.getThreadPoolName()).compare(localWebProp, webExecutorProperties);
        if (hasWebChanges) {
            webServerTpAdapter.refreshWeb(webExecutorProperties);
        }
        return hasWebChanges;
    }

    /**
     * 在本地配置中查找对应的线程池配置
     */
    private ExecutorProperties findLocalExecutorProperties(String threadPoolName) {
        if (localTpProperties == null || localTpProperties.getTpExecutors() == null) {
            return null;
        }
        return localTpProperties.getTpExecutors().stream()
                .filter(prop -> threadPoolName.equals(prop.getThreadPoolName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 应用配置到线程池
     */
    private void applyConfiguration(@Nullable Object bean, ExecutorProperties properties) {
        if (bean == null) {
            // 创建线程池
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

            executor.setThreadNamePrefix(properties.getThreadNamePrefix());
            executor.setCorePoolSize(properties.getCorePoolSize());
            executor.setMaxPoolSize(properties.getMaximumPoolSize());
            executor.setKeepAliveSeconds((int) properties.getUnit().toSeconds(properties.getKeepAliveTime()));
            executor.setQueueCapacity(properties.getQueueCapacity());
            executor.setRejectedExecutionHandler(createRejectedExecutionHandler(properties.getRejectedHandlerType()));

            executor.initialize();

            SpringUtil.getFactory().registerSingleton(properties.getThreadPoolName(), executor);
            return;
        }
        if (bean instanceof ThreadPoolExecutor executor) {
            ThreadPoolExecutor newExecutor = new ThreadPoolExecutor(
                    properties.getCorePoolSize(), properties.getMaximumPoolSize(), properties.getKeepAliveTime(), properties.getUnit(),
                    new LinkedBlockingQueue<>(properties.getQueueCapacity()),
                    new ThreadFactory() {
                        private final AtomicInteger threadNumber = new AtomicInteger(1);

                        @Override
                        public Thread newThread(Runnable r) {
                            Thread t = new Thread(r,
                                    properties.getThreadNamePrefix() + threadNumber.getAndIncrement());
                            if (t.isDaemon())
                                t.setDaemon(false);
                            if (t.getPriority() != Thread.NORM_PRIORITY)
                                t.setPriority(Thread.NORM_PRIORITY);
                            return t;
                        }
                    }
                    , createRejectedExecutionHandler(properties.getRejectedHandlerType()));

            newExecutor.prestartAllCoreThreads();

            // 移除旧bean并注册新的Bean
            DefaultListableBeanFactory beanFactory = SpringUtil.getFactory();

            if (beanFactory.containsSingleton(properties.getThreadPoolName())) {
                beanFactory.destroySingleton(properties.getThreadPoolName());
            }

            // 直接注册单例实例（推荐方式）
            beanFactory.registerSingleton(properties.getThreadPoolName(), newExecutor);

            // 确保Bean被正确初始化
            beanFactory.autowireBean(newExecutor);

            // 关闭旧线程池
            executor.shutdown();
        } else if (bean instanceof ThreadPoolTaskExecutor executor) {
            executor.setThreadNamePrefix(properties.getThreadNamePrefix());
            executor.setCorePoolSize(properties.getCorePoolSize());
            executor.setMaxPoolSize(properties.getMaximumPoolSize());
            executor.setKeepAliveSeconds((int) properties.getUnit().toSeconds(properties.getKeepAliveTime()));
            executor.setQueueCapacity(properties.getQueueCapacity());
            executor.setRejectedExecutionHandler(createRejectedExecutionHandler(properties.getRejectedHandlerType()));
        }
    }

    /**
     * 通过反射创建拒绝策略处理器
     */
    private RejectedExecutionHandler createRejectedExecutionHandler(String handlerType) {
        if (handlerType == null || handlerType.trim().isEmpty()) {
            log.warn("拒绝策略类型为空，使用默认策略");
            return new ThreadPoolExecutor.AbortPolicy();
        }

        try {
            String className = getRejectedHandlerClassName(handlerType);
            Class<?> handlerClass = Class.forName(className);
            return (RejectedExecutionHandler) handlerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error("创建拒绝策略'{}'失败，使用默认AbortPolicy: {}", handlerType, e.getMessage());
            return new ThreadPoolExecutor.AbortPolicy();
        }
    }

    /**
     * 根据配置的字符串获取完整的类名
     */
    private String getRejectedHandlerClassName(String handlerType) {
        switch (handlerType.toLowerCase()) {
            case "abortpolicy":
                return "java.util.concurrent.ThreadPoolExecutor$AbortPolicy";
            case "callerrunspolicy":
                return "java.util.concurrent.ThreadPoolExecutor$CallerRunsPolicy";
            case "discardpolicy":
                return "java.util.concurrent.ThreadPoolExecutor$DiscardPolicy";
            case "discardoldestpolicy":
                return "java.util.concurrent.ThreadPoolExecutor$DiscardOldestPolicy";
            default:
                // 如果是自定义的完整类名，直接返回
                if (handlerType.contains(".")) {
                    return handlerType;
                }
                log.warn("未知的拒绝策略类型: {}，使用默认AbortPolicy", handlerType);
                return "java.util.concurrent.ThreadPoolExecutor$AbortPolicy";
        }
    }

}
