package com.wzkris.auth.config;

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
    private int accessTokenTimeOut = 900;

    /**
     * refresh_token 有效期 （单位：秒）
     **/
    private int refreshTokenTimeOut = 86400;

    /**
     * 是否重复使用刷新令牌 false则每次签发新的刷新令牌
     */
    private Boolean reuseRefreshTokens = false;

    /**
     * authorization_code 有效期 （单位：秒）
     */
    private int authorizationCodeTimeOut = 1800;

    /**
     * device_code 有效期 （单位：秒）
     */
    private int deviceCodeTimeOut = 1800;

    /**
     * 自定义的用户token 有效期（单位：秒）
     */
    private int userTokenTimeOut = 1800;

    /**
     * 自定义的用户refresh_token 有效期（单位：秒）
     */
    private int userRefreshTokenTimeOut = 86400 * 3; // 默认3天

}
