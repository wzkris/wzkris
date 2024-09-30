package com.wzkris.common.openfeign.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : feign调用安全配置
 * @date : 2024/09/28 15:30
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "security.feign")
public class IdentityProperties {
    // 安全key
    private String identityKey;
    // 安全value
    private String identityValue;
}
