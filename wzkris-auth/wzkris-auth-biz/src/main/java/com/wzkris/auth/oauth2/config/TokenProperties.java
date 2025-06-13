package com.wzkris.auth.oauth2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : token配置参数
 * @date : 2023/8/8 16:36
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "token-config")
public class TokenProperties {

    /**
     * access_token 有效期（单位：秒）
     **/
    private int accessTokenTimeOut;

    /**
     * refresh_token 有效期 （单位：秒）
     **/
    private int refreshTokenTimeOut;

    /**
     * 是否重复使用刷新令牌 false则每次签发新的刷新令牌
     */
    private Boolean reuseRefreshTokens = false;

    /**
     * authorization_code 有效期 （单位：秒）
     */
    private int authorizationCodeTimeOut;

    /**
     * device_code 有效期 （单位：秒）
     */
    private int deviceCodeTimeOut;
}
