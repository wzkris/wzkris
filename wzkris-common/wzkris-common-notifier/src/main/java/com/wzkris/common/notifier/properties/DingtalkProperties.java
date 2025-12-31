package com.wzkris.common.notifier.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
@RefreshScope
@ConfigurationProperties(prefix = "dingtalk")
public class DingtalkProperties {

    /**
     * 应用Key
     */
    private String appKey;

    /**
     * 应用Secret
     */
    private String appSecret;

    /**
     * 自定义 AccessToken 获取地址（可选）
     */
    private String accessTokenUrl;

    /**
     * 机器人Code
     */
    private String robotCode;

    /**
     * 机器人Webhook（可选）
     */
    private String robotWebhook;

}
