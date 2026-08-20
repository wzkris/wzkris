package com.wzkris.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关风控验证码配置。
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "security.risk-captcha")
public class RiskCaptchaProperties {

    private boolean enabled = true;

    /**
     * 强制要求有效通行票的路径
     */
    private List<String> enforcedPaths = new ArrayList<>();

}
