package com.wzkris.auth.rmi;

import com.wzkris.auth.rmi.domain.req.TokenReq;
import com.wzkris.auth.rmi.domain.resp.TokenResponse;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.security.domain.AuthedClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RmiTokenFeignImpl implements RmiTokenFeign {

    private final TokenService tokenService;

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    @Override
    public TokenResponse checkOAuth2Token(TokenReq tokenReq) {
        OAuth2Authorization oAuth2Authorization =
                oAuth2AuthorizationService.findByToken(tokenReq.getToken(), OAuth2TokenType.ACCESS_TOKEN);
        if (oAuth2Authorization == null) {
            return TokenResponse.error(OAuth2ErrorCodes.INVALID_TOKEN, "token check failed");
        }

        OAuth2AccessToken accessToken = oAuth2Authorization.getAccessToken().getToken();

        if (accessToken.getExpiresAt() == null || accessToken.getExpiresAt().isBefore(Instant.now())) {
            return TokenResponse.error(OAuth2ErrorCodes.INVALID_TOKEN, "token check expired");
        }

        CorePrincipal principal = new AuthedClient(
                oAuth2Authorization.getRegisteredClientId(),
                this.buildScopes(oAuth2Authorization.getAuthorizedScopes()));
        return TokenResponse.ok(principal);
    }

    private Set<String> buildScopes(Set<String> authorizedScopes) {
        return authorizedScopes.stream().map(sc -> "SCOPE_" + sc).collect(Collectors.toSet());
    }

    @Override
    public TokenResponse checkUserToken(TokenReq tokenReq) {
        CorePrincipal principal = tokenService.loadByAccessToken(tokenReq.getToken());

        if (principal == null) {
            return TokenResponse.error(OAuth2ErrorCodes.INVALID_TOKEN, "token check failed");
        }

        return TokenResponse.ok(principal);
    }

}
