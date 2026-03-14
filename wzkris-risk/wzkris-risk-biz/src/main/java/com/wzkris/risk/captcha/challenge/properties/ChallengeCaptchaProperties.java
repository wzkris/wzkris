package com.wzkris.risk.captcha.challenge.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 挑战配置
 */
@Data
@ConfigurationProperties("captcha-challenge")
@Configuration
public class ChallengeCaptchaProperties {

    /**
     * 存储策略：redis / memory
     */
    private String store = "redis";

    private String challengePrefix = "captcha-challenge:";

    private String tokenPrefix = "captcha-token:";

    private int challengeCount = 50;

    private int challengeLength = 32;

    private int challengeDifficulty = 4;

    private long challengeExpiresMs = 90_000L;

    private long tokenExpiresMs = 120_000L;

    private int idSize = 16;

}
