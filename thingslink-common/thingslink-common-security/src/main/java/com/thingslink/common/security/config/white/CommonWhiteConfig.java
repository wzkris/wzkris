package com.thingslink.common.security.config.white;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 公共放行白名单配置
 *
 * @author wzkris
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "security.common-white")
public class CommonWhiteConfig {
    /**
     * 放行白名单配置
     */
    private List<String> urls = new ArrayList<>();
}
