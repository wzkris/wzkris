package com.wzkris.common.openfeign.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : okhttp参数
 * @date : 2025/06/10 15:00
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "feign-okhttp")
public class FeignClientProperties {

    private String timeUnit = "MILLISECONDS";

    private int connectTimeout = 1000;

    private int readTimeout = 1000;

    private int writeTimeout = 1000;

    private int callTimeout = 2000;

    private ConnectionPool connectionPool = new ConnectionPool();

    /**
     * okhttp连接池配置
     */
    @Data
    public static class ConnectionPool {

        private int maxIdleConnections = 20;

        private String timeUnit = "MINUTES";

        private int keepAliveDuration = 5;

    }

}
