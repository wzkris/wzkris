package com.wzkris.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : JWT密钥配置
 * @date : 2025/06/06 09:40
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "jwt-rs256")
public class JwtSecretProperties {

    private String publicKey;

    private String privateKey;

}
