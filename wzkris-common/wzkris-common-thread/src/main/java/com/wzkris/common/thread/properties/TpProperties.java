package com.wzkris.common.thread.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态线程池配置
 *
 * @author wzkris
 * @date 2025/8/8
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties("dynamic-threadpool")
public class TpProperties {

    /**
     * 业务线程池配置列表
     */
    private List<ExecutorProperties> executors = new ArrayList<>();

    /**
     * Tomcat 线程池配置
     */
    private TomcatThreadProperties tomcatExecutor = new TomcatThreadProperties();

    @Data
    public static class TomcatThreadProperties {

        /**
         * 最大线程数（maxThreads）
         */
        private Integer maxThreads;

        /**
         * 最小空闲线程数（minSpareThreads）
         */
        private Integer minSpareThreads;

        /**
         * 最大连接数（maxConnections）
         */
        private Integer maxConnections;

        /**
         * 接收队列长度（acceptCount）
         */
        private Integer acceptCount;

        /**
         * keepAlive 超时时间 毫秒
         */
        private Integer keepAliveTimeout;

        public boolean isEmpty() {
            return maxThreads == null
                    && minSpareThreads == null
                    && maxConnections == null
                    && acceptCount == null
                    && keepAliveTimeout == null;
        }

    }

}
