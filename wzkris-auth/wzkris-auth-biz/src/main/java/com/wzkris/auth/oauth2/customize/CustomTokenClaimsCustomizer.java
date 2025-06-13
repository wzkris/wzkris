package com.wzkris.auth.oauth2.customize;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.HashMap;
import java.util.Map;

public class CustomTokenClaimsCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    @Override
    public void customize(JwtEncodingContext context) {
        JwtClaimsSet.Builder claims = context.getClaims();

        // 添加自定义 claim
        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put("custom_claim", "custom_value");
        customClaims.put("user_role", "admin");

        // 将自定义 claims 添加到 JWT 中
        claims.claims(existingClaims -> existingClaims.putAll(customClaims));

        // 示例：添加用户特定信息
        if (context.getPrincipal().getAuthorities() != null) {
            claims.claim("authorities",
                    context.getPrincipal().getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList());
        }
    }

}
