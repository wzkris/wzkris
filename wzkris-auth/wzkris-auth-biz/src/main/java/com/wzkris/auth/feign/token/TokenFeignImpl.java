package com.wzkris.auth.feign.token;

import com.wzkris.auth.feign.token.req.TokenReq;
import com.wzkris.auth.feign.token.resp.TokenResponse;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.core.model.domain.LoginClient;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Hidden
@RestController
@RequestMapping("/feign-token")
@RequiredArgsConstructor
public class TokenFeignImpl implements TokenFeign {

    private final TokenService tokenService;

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    private final RegisteredClientRepository registeredClientRepository;

    @Override
    public TokenResponse<LoginClient> validateClient(TokenReq tokenReq) {
        OAuth2Authorization oAuth2Authorization =
                oAuth2AuthorizationService.findByToken(tokenReq.getToken(), OAuth2TokenType.ACCESS_TOKEN);
        if (oAuth2Authorization == null) {
            return TokenResponse.error(OAuth2ErrorCodes.INVALID_TOKEN, "token check failed");
        }

        OAuth2AccessToken accessToken = oAuth2Authorization.getAccessToken().getToken();

        if (accessToken.getExpiresAt() == null || accessToken.getExpiresAt().isBefore(Instant.now())) {
            return TokenResponse.error(OAuth2ErrorCodes.INVALID_TOKEN, "token check expired");
        }

        RegisteredClient registeredClient = registeredClientRepository
                .findByClientId(oAuth2Authorization.getRegisteredClientId());

        LoginClient loginClient = new LoginClient(Long.valueOf(registeredClient.getId()),
                this.buildScopes(oAuth2Authorization.getAuthorizedScopes()), registeredClient.getClientId());
        return TokenResponse.ok(loginClient);
    }

    private Set<String> buildScopes(Set<String> authorizedScopes) {
        return authorizedScopes.stream().map(sc -> "SCOPE_" + sc).collect(Collectors.toSet());
    }

    @Override
    public TokenResponse<MyPrincipal> validatePrincipal(TokenReq tokenReq) {
        MyPrincipal principal = tokenService.loadByAccessToken(tokenReq.getToken());

        if (principal == null) {
            return TokenResponse.error(OAuth2ErrorCodes.INVALID_TOKEN, "token check failed");
        }

        return TokenResponse.ok(principal);
    }

}
