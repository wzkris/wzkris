package com.wzkris.common.notifier.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties("dingtalk")
public class DingtalkProperties {

    private List<AppProperties> apps;

    @Data
    public static class AppProperties {

        private String appName;

        private String appKey;

        private String appSecret;

        private String accessTokenUrl;

        private String robotCode;

        private String robotWebhook;

    }

}
