package com.wzkris.common.remote.config;

import com.wzkris.common.remote.interceptor.DefaultInterceptor;
import com.wzkris.common.remote.properties.RemoteInterfaceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
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

    @Bean
    public DefaultInterceptor defaultInterceptor(ApplicationEventPublisher publisher) {
        return new DefaultInterceptor(publisher);
    }

    /**
     * 专供 @RemoteInterface 使用，带负载均衡（按 serviceId 解析）。
     * 仅通过 bean 名 "customRestClientBuilder" 注入；网关转发使用框架默认的 RestClient.Builder。
     * 本配置在 RestClientAutoConfiguration 之后执行，避免覆盖框架默认 Builder。
     */
    @Bean("customRestClientBuilder")
    @LoadBalanced
    public RestClient.Builder customRestClientBuilder(
            RemoteInterfaceProperties httpClientProperties,
            ObjectProvider<List<HttpMessageConverter<?>>> messageConvertersObjectProvider,
            ObjectProvider<ClientHttpRequestInterceptor> interceptorObjectProvider) {
        RestClient.Builder builder = RestClient.builder()
                .requestFactory(buildFactory(httpClientProperties))
                .defaultStatusHandler(status -> true, (request, response) -> {
                });

        List<ClientHttpRequestInterceptor> interceptors = interceptorObjectProvider.orderedStream()
                // @LoadBalanced 会通过框架机制补充 LB 拦截器，这里避免重复添加
                .filter(interceptor -> !isFrameworkLoadBalancerInterceptor(interceptor))
                .toList();
        if (CollectionUtils.isNotEmpty(interceptors)) {
            builder.requestInterceptors(list -> list.addAll(interceptors));
            log.info("加载到http service client拦截器，总数：{}，列表：{}",
                    interceptors.size(),
                    interceptors.stream()
                            .map(interceptor -> interceptor.getClass().getSimpleName())
                            .toList());
        }

        List<HttpMessageConverter<?>> messageConverters = messageConvertersObjectProvider.getIfAvailable();
        if (CollectionUtils.isNotEmpty(messageConverters)) {
            builder.messageConverters(messageConverters);
        }

        return builder;
    }

    private static boolean isFrameworkLoadBalancerInterceptor(ClientHttpRequestInterceptor interceptor) {
        Class<?> clazz = interceptor.getClass();
        return clazz.getPackageName().startsWith("org.springframework.cloud.client.loadbalancer")
                && clazz.getSimpleName().contains("LoadBalancer");
    }

    private static BufferingClientHttpRequestFactory buildFactory(RemoteInterfaceProperties httpClientProperties) {
        // 根据 connectionPool 配置创建 Executor
        ThreadFactory threadFactory = Thread.ofVirtual().name("wzkris-http-client-", 0)
                .uncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        log.error("http-client调用发生异常", e);
                    }
                })
                .factory();

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.of(httpClientProperties.getConnectTimeout(), TimeUnit.valueOf(httpClientProperties.getTimeUnit()).toChronoUnit()))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .executor(Executors.newThreadPerTaskExecutor(threadFactory))
                .build();

        // 使用自定义 HttpClient 创建 JdkClientHttpRequestFactory
        // readTimeout 在 JdkClientHttpRequestFactory 中设置（请求级别的超时）
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(httpClientProperties.getReadTimeout());

        return new BufferingClientHttpRequestFactory(factory);
    }

}
