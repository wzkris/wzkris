package com.thingslink.common.security.oauth2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

/**
 * 公共放行白名单配置
 *
 * @author wzkris
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "security.white")
public class PermitUrlProperties {

    /**
     * 公共配置
     */
    private List<String> commonUrls;

    /**
     * 自定义配置
     */
    private List<String> customUrls;

}
