package com.wzkris.auth.security.oauth2.customize;

import com.wzkris.common.core.constant.JwtClaimConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.BaseLoginUser;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * 额外jwt参数添加
 *
 * @author wzkris
 * @date 2025/06/15
 */
public class CustomTokenClaimsCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    @Override
    public void customize(JwtEncodingContext context) {
        if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
            return;
        }
        JwtClaimsSet.Builder claims = context.getClaims();
        String authType = null;
        var principal = context.getPrincipal();

        // OAuth2 客户端（例如 client_credentials）
        if (principal instanceof OAuth2ClientAuthenticationToken) {
            authType = AuthTypeEnum.CLIENT.getValue();
        } else if (principal != null && principal.getPrincipal() instanceof BaseLoginUser baseLoginUser) {
            authType = baseLoginUser.getAuthType().getValue();
        }

        if (authType != null) {
            claims.claim(JwtClaimConstants.AUTH_TYPE, authType);
        }
    }

}
