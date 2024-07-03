package com.wzkris.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : token配置
 * @date : 2023/8/8 16:36
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "token-config")
public class TokenConfig {
    /**
     * access_token 有效期（单位：秒）
     **/
    private int accessTokenTimeOut;
    /**
     * refresh_token 有效期 （单位：秒）
     **/
    private int refreshTokenTimeOut;
    /**
     * authorization_code 有效期 （单位：秒）
     */
    private int authorizationCodeTimeOut;
    /**
     * device_code 有效期 （单位：秒）
     */
    private int deviceCodeTimeOut;
}
