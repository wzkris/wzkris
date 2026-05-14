package com.wzkris.captcha.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 风控通行票（与网关 {@code RiskCaptchaFilter} 共用 Redis 前缀与实例，须与网关共用同一 Redis）。
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "captcha.risk-pass")
public class RiskPassProperties {

    /**
     * 通行票 TTL（秒）
     */
    private long passTtlSeconds = 1800;

    /**
     * 通行票 Redis key 前缀（值：clientKey）
     */
    private String passKeyPrefix = "gateway:risk-pass:";

    /**
     * 全站风控锁 Redis key 前缀（存在即要求携带通行票）
     */
    private String lockKeyPrefix = "gateway:risk-lock:";

}
