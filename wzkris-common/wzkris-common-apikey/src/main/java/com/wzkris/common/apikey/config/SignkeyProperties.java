package com.wzkris.common.apikey.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "signature")
public class SignkeyProperties {

    /**
     * key为服务名 value为密钥
     */
    private Map<String, Sign> keys = new HashMap<>();

    @Data
    public static class Sign {

        /**
         * 密钥
         */
        private String secret;

        /**
         * 最大间隔时间 // ms
         */
        private long maxInterval = 10_000;

    }

}
