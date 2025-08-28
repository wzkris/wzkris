package com.wzkris.monitor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : OAuth2客户端配置
 * @date : 2025/05/21 17:15
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "oauth2-client")
public class OAuth2ClientProperties {

    /**
     * 授权服务器url
     */
    private String url;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 密钥
     */
    private String clientSecret;

    /**
     * 授权
     */
    private List<String> scopes;

}
