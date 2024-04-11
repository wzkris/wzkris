package com.thingslink.common.third.dingtalk.config;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 钉钉配置
 * @date : 2023/5/20 11:26
 */
@Data
@FieldNameConstants
@Configuration
@ConfigurationProperties(prefix = "dingtalk")
public class DingTalkConfig {
    private String agentId;
    private String appKey;
    private String appSecret;
    private String corpId;
    private String apiToken;
}
