package com.wzkris.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;

/**
 * JWT 配置类
 * 配置 JwtDecoder 通过 JWK Set URI 从 auth 服务获取公钥
 *
 * @author wzkris
 */
@Slf4j
@Configuration
public class JwtConfiguration {

    /**
     * 配置 JwtDecoder
     * 使用 issuer location 方式，Spring Security 会自动：
     * 1. 从 JWT 的 iss claim 获取 issuer
     * 2. 从 Provider Configuration 端点发现 JWK Set URI ({issuer}/.well-known/openid-configuration)
     * 3. 从 JWK Set URI 获取公钥
     * 4. 验证 JWT 的 iss claim 是否匹配配置的 issuer
     *
     * @return JwtDecoder
     */
    @Bean
    public JwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) {
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }

}
