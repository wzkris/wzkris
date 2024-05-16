package com.thingslink.common.security.oauth2.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : OAuth2 feign请求拦截器
 * @date : 2024/5/16 16:57
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "security.oauth2")
public class OAuth2Properties {
    // 自省uri
    private String introspectionUri;
    // 获取token的客户端地址
    private String clientTokenUri;
    // 客户端id
    private String clientid;
    // 客户端密钥
    private String clientSecret;
}
