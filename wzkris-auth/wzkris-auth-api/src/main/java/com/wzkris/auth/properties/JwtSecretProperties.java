package com.wzkris.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "jwt-rs256")
public class JwtSecretProperties {

    /**
     * 当前公钥
     */
    private String publicKey;

    /**
     * 当前私钥
     */
    private String privateKey;

    /**
     * 历史公钥（用于密钥轮换期间验证旧 JWT）
     * 可选配置，仅在密钥轮换期间需要
     */
    private String previousPublicKey;

    /**
     * 历史私钥（用于密钥轮换期间验证旧 JWT）
     * 可选配置，仅在密钥轮换期间需要
     */
    private String previousPrivateKey;

}
