package com.wzkris.common.httpservice.config;

import com.wzkris.common.httpservice.annotation.EnableHttpServiceClients;
import com.wzkris.common.httpservice.interceptor.PublishEventInterceptorPostProcessor;
import com.wzkris.common.httpservice.interceptor.core.HttpServiceClientInterceptor;
import com.wzkris.common.httpservice.properties.HttpServiceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
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

/**
 * 自动配置 HTTP service clients.
 */
@Slf4j
@Import({HttpServiceClientInterceptor.class, PublishEventInterceptorPostProcessor.class})
@EnableConfigurationProperties({HttpServiceProperties.class})
@EnableHttpServiceClients
@AutoConfiguration
public class HttpServiceClientAutoConfiguration {

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

    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean
    public RestClient.Builder httpServiceRestClientBuilder(
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

