package com.thingslink.common.security.oauth2.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : OAuth2 安全配置
 * @date : 2024/5/16 16:57
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "security")
public class OAuth2Properties {
    // token自省uri
    private String introspectionUri;
    // 身份key
    private String identityKey;
    // 身份value
    private String identityValue;
    // 白名单url
    private White white;

    @Data
    public static class White {
        /**
         * 公共配置
         */
        private List<String> commonUrls;

        /**
         * 自定义配置
         */
        private List<String> customUrls;
    }
}
