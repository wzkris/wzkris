package com.wzkris.common.httpclient.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : http service参数
 * @date : 2025/06/10 15:00
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "http-client")
public class HttpClientProperties {

    private String timeUnit = "MILLISECONDS";

    private int connectTimeout = 10_000;

    private int readTimeout = 10_000;

}
