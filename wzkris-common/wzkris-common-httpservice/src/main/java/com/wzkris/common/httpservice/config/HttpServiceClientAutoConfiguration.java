package com.wzkris.common.httpservice.config;

import com.wzkris.common.httpservice.annotation.EnableHttpServiceClients;
import com.wzkris.common.httpservice.interceptor.PublishEventInterceptorPostProcessor;
import com.wzkris.common.httpservice.interceptor.core.HttpServiceClientInterceptor;
import com.wzkris.common.httpservice.properties.HttpServiceProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * 自动配置 HTTP service clients.
 */
@Import({HttpServiceClientInterceptor.class, PublishEventInterceptorPostProcessor.class})
@EnableConfigurationProperties({HttpServiceProperties.class})
@EnableHttpServiceClients
@AutoConfiguration
public class HttpServiceClientAutoConfiguration {

}
