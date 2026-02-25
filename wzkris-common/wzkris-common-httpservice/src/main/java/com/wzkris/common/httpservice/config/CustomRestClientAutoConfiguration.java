package com.wzkris.common.httpservice.config;

import com.wzkris.common.httpservice.interceptor.core.HttpServiceClientInterceptor;
import com.wzkris.common.httpservice.properties.HttpServiceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Slf4j
@AutoConfiguration(after = RestClientAutoConfiguration.class)
public class CustomRestClientAutoConfiguration {

    private static BufferingClientHttpRequestFactory buildFactory(HttpServiceProperties httpServiceProperties) {
        // 根据 connectionPool 配置创建 Executor
        ThreadFactory threadFactory = Thread.ofVirtual().name("http-service-client-", 0)
                .uncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        log.error("http-service调用发生异常", e);
                    }
                })
                .factory();

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.of(httpServiceProperties.getConnectTimeout(), TimeUnit.valueOf(httpServiceProperties.getTimeUnit()).toChronoUnit()))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .executor(Executors.newThreadPerTaskExecutor(threadFactory))
                .build();

        // 使用自定义 HttpClient 创建 JdkClientHttpRequestFactory
        // readTimeout 在 JdkClientHttpRequestFactory 中设置（请求级别的超时）
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(httpServiceProperties.getReadTimeout());

        return new BufferingClientHttpRequestFactory(factory);
    }

    /**
     * 专供 @HttpServiceClient 使用，带负载均衡（按 serviceId 解析）。
     * 仅通过 bean 名 "customRestClientBuilder" 注入；网关转发使用框架默认的 RestClient.Builder。
     * 本配置在 RestClientAutoConfiguration 之后执行，避免覆盖框架默认 Builder。
     */
    @Bean("customRestClientBuilder")
    @LoadBalanced
    public RestClient.Builder customRestClientBuilder(
            HttpServiceProperties httpServiceProperties,
            ObjectProvider<List<HttpMessageConverter<?>>> messageConvertersObjectProvider,
            HttpServiceClientInterceptor httpServiceClientInterceptor) {
        RestClient.Builder builder = RestClient.builder()
                .requestFactory(buildFactory(httpServiceProperties))
                .requestInterceptor(httpServiceClientInterceptor);

        List<HttpMessageConverter<?>> messageConverters = messageConvertersObjectProvider.getIfAvailable();
        if (CollectionUtils.isNotEmpty(messageConverters)) {
            builder.messageConverters(messageConverters);
        }

        return builder;
    }

}
