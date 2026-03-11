package com.wzkris.common.httpclient.config;

import com.wzkris.common.httpclient.annotation.EnableHttpClients;
import com.wzkris.common.httpclient.properties.HttpClientProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 自动配置 HTTP service clients.
 */
@EnableConfigurationProperties({HttpClientProperties.class})
@EnableHttpClients
@AutoConfiguration
public class HttpClientAutoConfiguration {

}
