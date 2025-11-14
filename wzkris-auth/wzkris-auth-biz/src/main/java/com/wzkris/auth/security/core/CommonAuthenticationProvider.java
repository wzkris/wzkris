package com.wzkris.auth.security.core;

import com.wzkris.auth.security.config.TokenProperties;
import com.wzkris.auth.security.core.refresh.RefreshAuthenticationToken;
import com.wzkris.auth.service.TokenService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description provider基类，验证Authentication
 */
public abstract class CommonAuthenticationProvider<T extends CommonAuthenticationToken>
        implements AuthenticationProvider {

    private final RegisteredClient registeredClient = RegisteredClient.withId("default-client")
            .clientId("default-client")
            .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .build();

    private final TokenProperties tokenProperties;

    private final TokenService tokenService;

    protected CommonAuthenticationProvider(
            TokenProperties tokenProperties,
            TokenService tokenService) {
        this.tokenProperties = tokenProperties;
        this.tokenService = tokenService;
    }

    /**
     * 认证核心方法
     */
    protected abstract T doAuthenticate(Authentication authentication);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        T authenticationToken = this.doAuthenticate(authentication);

        return this.buildOAuth2AccessTokenAuthenticationToken(authenticationToken);
    }

    final OAuth2AccessTokenAuthenticationToken buildOAuth2AccessTokenAuthenticationToken(T authenticationToken) {
        String generatedToken = tokenService.generateToken(authenticationToken.getPrincipal());
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedToken, Instant.now(), Instant.now().plus(tokenProperties.getUserTokenTimeOut(), ChronoUnit.SECONDS));

        String refreshToken;
        if (authenticationToken instanceof RefreshAuthenticationToken refreshAuthenticationToken) {
            refreshToken = refreshAuthenticationToken.getRefreshToken();
        } else {
            refreshToken = tokenService.generateToken();
        }

        tokenService.save(authenticationToken.getPrincipal(), generatedToken, refreshToken);

        OAuth2RefreshToken oAuth2RefreshToken = new OAuth2RefreshToken(
                refreshToken, Instant.now(), Instant.now().plus(tokenProperties.getUserRefreshTokenTimeOut(), ChronoUnit.SECONDS));
        OAuth2AccessTokenAuthenticationToken oAuth2AccessTokenAuthenticationToken =
                new OAuth2AccessTokenAuthenticationToken(
                        registeredClient, authenticationToken, accessToken, oAuth2RefreshToken);
        oAuth2AccessTokenAuthenticationToken.setAuthenticated(true);

        return oAuth2AccessTokenAuthenticationToken;
    }

}
