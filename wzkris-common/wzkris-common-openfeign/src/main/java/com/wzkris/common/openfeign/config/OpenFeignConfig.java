package com.wzkris.common.openfeign.config;

import com.wzkris.common.openfeign.core.TargeterDecorator;
import com.wzkris.common.openfeign.handler.FeignRequestContextInterceptor;
import com.wzkris.common.openfeign.handler.FeignRequestInterceptor;
import com.wzkris.common.openfeign.handler.FeignResponseInterceptor;
import feign.RequestInterceptor;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.Targeter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : restTemplate配置类
 * @date : 2022/12/1 10:02
 */
@EnableFeignClients(basePackages = "com.wzkris.**")
public class OpenFeignConfig implements ApplicationContextAware, BeanPostProcessor {

    private final FeignClientProperties properties;

    private ApplicationContext applicationContext;

    public OpenFeignConfig(FeignClientProperties properties) {
        this.properties = properties;
    }

    @Bean
    public okhttp3.OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
                // 连接池配置
                .connectionPool(new ConnectionPool(
                                properties.getConnectionPool().getMaxIdleConnections(),
                                properties.getConnectionPool().getKeepAliveDuration(),
                                TimeUnit.valueOf(properties.getConnectionPool().getTimeUnit())
                        )
                )
                // HTTP2连接心跳
                .pingInterval(properties.getPingInterval(), TimeUnit.valueOf(properties.getTimeUnit()))
                // 连接超时
                .connectTimeout(properties.getConnectTimeout(), TimeUnit.valueOf(properties.getTimeUnit()))
                // 读取超时
                .readTimeout(properties.getReadTimeout(), TimeUnit.valueOf(properties.getTimeUnit()))
                // 写入超时
                .writeTimeout(properties.getWriteTimeout(), TimeUnit.valueOf(properties.getTimeUnit()))
                // 全链路超时
                .callTimeout(properties.getCallTimeout(), TimeUnit.valueOf(properties.getTimeUnit()))
                .build();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Targeter targeter) {
            return new TargeterDecorator(targeter, applicationContext);
        }
        return bean;
    }

    @Bean
    public FeignResponseInterceptor responseInterceptor() {
        return new FeignResponseInterceptor();
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new FeignRequestInterceptor();
    }

    @Bean
    @ConditionalOnClass(UsernamePasswordAuthenticationToken.class)
    public RequestInterceptor requestAuthenticationInterceptor() {
        return new FeignRequestContextInterceptor();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
