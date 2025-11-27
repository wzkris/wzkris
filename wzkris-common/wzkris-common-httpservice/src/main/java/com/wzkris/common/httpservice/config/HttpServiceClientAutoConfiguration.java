package com.wzkris.common.httpservice.config;

import com.wzkris.common.core.threads.TracingIdRunnable;
import com.wzkris.common.httpservice.annotation.EnableHttpServiceClients;
import com.wzkris.common.httpservice.interceptor.HttpServiceClientInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自动配置 HTTP service clients.
 */
@Import(HttpServiceClientInterceptor.class)
@EnableConfigurationProperties({HttpServiceProperties.class})
@EnableHttpServiceClients
@AutoConfiguration
public class HttpServiceClientAutoConfiguration {

    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean
    public RestClient.Builder httpServiceRestClientBuilder(
            HttpServiceProperties httpServiceProperties,
            HttpServiceClientInterceptor httpServiceClientInterceptor) {
        return RestClient.builder()
                .requestFactory(buildFactory(httpServiceProperties))
                .requestInterceptor(httpServiceClientInterceptor);
    }

    private static BufferingClientHttpRequestFactory buildFactory(HttpServiceProperties httpServiceProperties) {
        // 根据 connectionPool 配置创建 Executor
        HttpServiceProperties.ConnectionPool connectionPool = httpServiceProperties.getConnectionPool();
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(new TracingIdRunnable(r), "http-service-client-" + threadNumber.getAndIncrement());
                t.setDaemon(true);
                return t;
            }
        };

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.of(httpServiceProperties.getConnectTimeout(), TimeUnit.valueOf(httpServiceProperties.getTimeUnit()).toChronoUnit()))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .executor(Executors.newFixedThreadPool(connectionPool.getMaxIdleConnections(), threadFactory))
                .build();

        // 使用自定义 HttpClient 创建 JdkClientHttpRequestFactory
        // readTimeout 在 JdkClientHttpRequestFactory 中设置（请求级别的超时）
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(httpServiceProperties.getReadTimeout());

        return new BufferingClientHttpRequestFactory(factory);
    }

}

