package com.wzkris.captcha.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "risk-captcha")
public class RiskPassProperties {

    private long passTtlSeconds = 180;

}
