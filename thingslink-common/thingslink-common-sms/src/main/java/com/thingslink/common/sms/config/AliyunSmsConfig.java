package com.thingslink.common.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 阿里云短信配置
 * @date : 2023/5/13 8:07
 */
@Data
@Component
@ConfigurationProperties(prefix = "sms.aliyun")
public class AliyunSmsConfig {
    private String regionId;
    private String accessKey;
    private String accessSecret;
    private String signName;
}
