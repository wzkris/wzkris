package com.wzkris.common.remote.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : remote interface参数
 * @date : 2025/06/10 15:00
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "remote-interface")
public class RemoteInterfaceProperties {

    private String timeUnit = "MILLISECONDS";

    private int connectTimeout = 10_000;

    private int readTimeout = 10_000;

}
