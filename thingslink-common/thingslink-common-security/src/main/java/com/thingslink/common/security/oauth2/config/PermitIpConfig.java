package com.thingslink.common.security.oauth2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author wzkris
 * @description 允许访问子服务的ip配置
 * @date 2024-04-11
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "security.ip")
public class PermitIpConfig {
    // 是否启用ip过滤
    private Boolean enable;
    // 允许ip集合
    private List<String> ipList;
}
