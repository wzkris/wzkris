package com.wzkris.gateway.config;

import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.HttpProtocol;

/**
 * 支持HTTP/2.0
 *
 * @author wzkris
 */
@Configuration
public class HttpClientConfiguration {

    /**
     * HttpClient 配置支持 H2C 协议
     *
     * @return org.springframework.cloud.gateway.config.HttpClientCustomizer
     * @date 2025/06/19
     */
    @Bean
    public HttpClientCustomizer httpClientCustomizer() {
        return httpClient -> httpClient.protocol(HttpProtocol.H2, HttpProtocol.H2C);
    }

}
