package com.wzkris.common.captcha.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * cap 属性配置
 *
 * @author wuhunyu
 * @date 2025/06/16 11:16
 **/
@Data
@NoArgsConstructor
@ConfigurationProperties("captcha")
public class CapProperties {

    /**
     * 存储类型
     */
    private String storeType = "REDIS";

    /**
     * 前缀
     */
    private String captchaPrefix = "captcha:";

    /**
     * 挑战数量
     */
    private int challengeCount = 50;

    /**
     * 挑战大小
     */
    private int challengeSize = 32;

    /**
     * 挑战难度
     */
    private int challengeDifficulty = 4;

    /**
     * 挑战过期时间，单位：毫秒
     */
    private long challengeExpiresMs = 90_000L;

    /**
     * token 过期时间，单位：毫秒
     */
    private long tokenExpiresMs = 120_000L;

    /**
     * id 大小
     */
    private int idSize = 16;

}
