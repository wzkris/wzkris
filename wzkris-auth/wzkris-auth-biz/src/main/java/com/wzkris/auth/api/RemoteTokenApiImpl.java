package com.wzkris.auth.api;

import com.wzkris.auth.api.domain.request.TokenReq;
import com.wzkris.auth.api.domain.response.TokenResponse;
import com.wzkris.common.openfeign.annotation.InnerAuth;
import com.wzkris.common.security.oauth2.domain.AuthBaseUser;
import com.wzkris.common.security.oauth2.domain.model.AuthThings;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.Instant;

@Hidden
@InnerAuth
@RestController
@RequiredArgsConstructor
public class RemoteTokenApiImpl implements RemoteTokenApi {

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    @Override
    public TokenResponse checkToken(TokenReq tokenReq) {
        OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService.findByToken(tokenReq.getToken(), OAuth2TokenType.ACCESS_TOKEN);
        if (oAuth2Authorization == null) {
            return TokenResponse.failed(OAuth2ErrorCodes.INVALID_TOKEN, "token check failed");
        }

        OAuth2AccessToken accessToken = oAuth2Authorization.getAccessToken().getToken();

        if (accessToken.getExpiresAt() == null || accessToken.getExpiresAt().isBefore(Instant.now())) {
            return TokenResponse.failed(OAuth2ErrorCodes.INVALID_TOKEN, "token check expired");
        }

        UsernamePasswordAuthenticationToken authenticationToken = oAuth2Authorization.getAttribute(Principal.class.getName());

        AuthBaseUser baseUser;
        if (authenticationToken == null) {
            baseUser = new AuthThings(oAuth2Authorization.getPrincipalName(), oAuth2Authorization.getAuthorizedScopes());
        } else {
            baseUser = (AuthBaseUser) authenticationToken.getPrincipal();
        }
        return TokenResponse.ok(baseUser);
    }
}
