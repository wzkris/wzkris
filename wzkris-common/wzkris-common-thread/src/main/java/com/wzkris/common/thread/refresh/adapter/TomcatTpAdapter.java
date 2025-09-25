package com.wzkris.common.thread.refresh.adapter;

import com.wzkris.common.thread.properties.ExecutorProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.ProtocolHandler;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@ConditionalOnClass(TomcatWebServer.class)
public class TomcatTpAdapter extends WebServerTpAdapter {

    public TomcatTpAdapter(ServletWebServerApplicationContext servletWebServerApplicationContext) {
        super(servletWebServerApplicationContext);
    }

    @Override
    public void refreshWeb(ExecutorProperties properties) {
        if (properties == null) {
            return;
        }
        ProtocolHandler protocolHandler = ((TomcatWebServer) webServer).getTomcat().getConnector().getProtocolHandler();

        // 创建拒绝策略
        ThreadPoolExecutor.RejectedExecutionHandler rejectedHandler = createRejectedExecutionHandler(properties.getRejectedHandlerType());

        ThreadPoolExecutor newExecutor = new ThreadPoolExecutor(properties.getCorePoolSize(), properties.getMaximumPoolSize(),
                properties.getKeepAliveTime(), properties.getUnit(), new LinkedBlockingQueue<>(properties.getQueueCapacity()),
                rejectedHandler);

        newExecutor.prestartAllCoreThreads();// 预启动线程池

        ThreadPoolExecutor oldExecutor = (ThreadPoolExecutor) protocolHandler.getExecutor();// 取出旧线程池

        protocolHandler.setExecutor(newExecutor);
        log.info("tomcat线程池刷新成功，刷新参数：{}", properties);

        shutdownOldExecutor(oldExecutor);
    }

    /**
     * 通过反射创建拒绝策略处理器（Tomcat专用）
     */
    private ThreadPoolExecutor.RejectedExecutionHandler createRejectedExecutionHandler(String handlerType) {
        if (handlerType == null || handlerType.trim().isEmpty()) {
            log.warn("Tomcat拒绝策略类型为空，使用默认AbortPolicy");
            return new ThreadPoolExecutor.AbortPolicy();
        }

        try {
            String className = getTomcatRejectedHandlerClassName(handlerType);
            Class<?> handlerClass = Class.forName(className);

            // 特殊处理：Tomcat的拒绝策略可能需要不同的实例化方式
            return (ThreadPoolExecutor.RejectedExecutionHandler) handlerClass.getDeclaredConstructor().newInstance();

        } catch (ClassNotFoundException e) {
            log.error("Tomcat拒绝策略类未找到: {}，使用默认AbortPolicy", handlerType);
        } catch (Exception e) {
            log.error("创建Tomcat拒绝策略'{}'失败: {}，使用默认AbortPolicy",
                    handlerType, e.getMessage());
        }

        return new ThreadPoolExecutor.AbortPolicy();
    }

    /**
     * 获取Tomcat拒绝策略的完整类名
     */
    private String getTomcatRejectedHandlerClassName(String handlerType) {
        // 如果是完整类名，直接返回
        if (handlerType.contains(".")) {
            return handlerType;
        }

        // 内置策略映射
        return switch (handlerType.toLowerCase()) {
            case "abortpolicy" -> "org.apache.tomcat.util.threads.ThreadPoolExecutor$AbortPolicy";
            case "callerrunspolicy" -> "org.apache.tomcat.util.threads.ThreadPoolExecutor$CallerRunsPolicy";
            case "discardpolicy" -> "org.apache.tomcat.util.threads.ThreadPoolExecutor$DiscardPolicy";
            case "discardoldestpolicy" -> "org.apache.tomcat.util.threads.ThreadPoolExecutor$DiscardOldestPolicy";
            default -> {
                log.warn("未知拒绝策略类型: {}，使用JDK标准AbortPolicy", handlerType);
                yield "java.util.concurrent.ThreadPoolExecutor$AbortPolicy";
            }
        };
    }

    private void shutdownOldExecutor(ThreadPoolExecutor oldExecutor) {
        try {
            oldExecutor.shutdown();
            // 等待一段时间让现有任务完成
            if (!oldExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                log.warn("旧Tomcat线程池关闭超时，强制终止");
                oldExecutor.shutdownNow();
                if (!oldExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                    log.error("旧Tomcat线程池未能正常终止");
                }
            } else {
                log.info("旧Tomcat线程池已优雅关闭");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("关闭旧线程池时被中断, errmsg: {}", e.getMessage(), e);
            oldExecutor.shutdownNow();
        }
    }

}
