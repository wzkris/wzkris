package com.wzkris.common.remote.config;

import com.wzkris.common.remote.interceptor.DefaultInterceptor;
import com.wzkris.common.remote.interceptor.RemoteClientRequestInterceptor;
import com.wzkris.common.remote.properties.RemoteInterfaceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.util.concurrent.Executor;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Slf4j
@AutoConfiguration(after = RestClientAutoConfiguration.class)
public class CustomRestClientAutoConfiguration {

    static final String REMOTE_REST_CLIENT_BUILDER_BEAN_NAME = "remoteRestClientBuilder";

    @Bean
    public DefaultInterceptor defaultInterceptor(
            ApplicationEventPublisher publisher,
            RemoteInterfaceProperties remoteInterfaceProperties) {
        return new DefaultInterceptor(publisher, remoteInterfaceProperties);
    }

    @Bean(REMOTE_REST_CLIENT_BUILDER_BEAN_NAME)
    public RestClient.Builder remoteRestClientBuilder(
            RemoteInterfaceProperties httpClientProperties,
            ObjectProvider<HttpMessageConverter<?>> messageConverterObjectProvider,
            ObjectProvider<RemoteClientRequestInterceptor> interceptorObjectProvider) {
        return createRemoteRestClientBuilder(
                httpClientProperties,
                messageConverterObjectProvider,
                interceptorObjectProvider
        );
    }

    private RestClient.Builder createRemoteRestClientBuilder(
            RemoteInterfaceProperties httpClientProperties,
            ObjectProvider<HttpMessageConverter<?>> messageConverterObjectProvider,
            ObjectProvider<RemoteClientRequestInterceptor> interceptorObjectProvider) {
        RestClient.Builder builder = RestClient.builder()
                .requestFactory(buildFactory(httpClientProperties))
                .defaultStatusHandler(status -> true, (request, response) -> {
                });

        List<RemoteClientRequestInterceptor> interceptors = interceptorObjectProvider.orderedStream()
                .toList();
        if (CollectionUtils.isNotEmpty(interceptors)) {
            builder.requestInterceptors(list -> list.addAll(interceptors));
            log.info("加载到http service client拦截器，总数：{}，列表：{}",
                    interceptors.size(),
                    interceptors.stream()
                            .map(interceptor -> interceptor.getClass().getSimpleName())
                            .toList());
        }

        List<HttpMessageConverter<?>> messageConverters = messageConverterObjectProvider.orderedStream()
                .toList();
        if (CollectionUtils.isNotEmpty(messageConverters)) {
            builder.messageConverters(list -> list.addAll(0, messageConverters));
        }

        return builder;
    }

    private static BufferingClientHttpRequestFactory buildFactory(RemoteInterfaceProperties httpClientProperties) {
        RemoteInterfaceProperties.HttpProperties httpProperties = httpClientProperties.getHttp();
        Executor customExecutor = buildCustomExecutor(httpProperties);
        HttpClient.Builder httpClientBuilder = HttpClient.newBuilder()
                .connectTimeout(httpProperties.getConnectTimeoutDuration())
                .followRedirects(httpProperties.getFollowRedirectsPolicy())
                .executor(customExecutor);
        HttpClient httpClient = httpClientBuilder.build();

        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(httpProperties.getReadTimeoutDuration());

        return new BufferingClientHttpRequestFactory(factory);
    }

    private static Executor buildCustomExecutor(RemoteInterfaceProperties.HttpProperties httpProperties) {
        Thread.Builder threadBuilder = Thread.ofVirtual();
        if (StringUtils.hasText(httpProperties.getExecutorThreadNamePrefix())) {
            threadBuilder = threadBuilder.name(httpProperties.getExecutorThreadNamePrefix(), 0);
        }

        ThreadFactory threadFactory = threadBuilder
                .uncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        log.error("http-client调用发生异常", e);
                    }
                })
                .factory();
        return Executors.newThreadPerTaskExecutor(threadFactory);
    }

}
