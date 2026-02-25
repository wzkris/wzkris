package com.wzkris.common.httpclient.config;

import com.wzkris.common.httpclient.annotation.EnableHttpClients;
import com.wzkris.common.httpclient.interceptor.PublishEventInterceptorPostProcessor;
import com.wzkris.common.httpclient.interceptor.core.HttpClientInterceptor;
import com.wzkris.common.httpclient.properties.HttpClientProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * 自动配置 HTTP service clients.
 */
@Import({HttpClientInterceptor.class, PublishEventInterceptorPostProcessor.class})
@EnableConfigurationProperties({HttpClientProperties.class})
@EnableHttpClients
@AutoConfiguration
public class HttpClientAutoConfiguration {

}
