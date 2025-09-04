package com.wzkris.auth.security.core;

import com.wzkris.auth.rmi.enums.AuthenticatedType;
import com.wzkris.auth.security.config.TokenProperties;
import com.wzkris.auth.security.core.refresh.RefreshAuthenticationToken;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.domain.CorePrincipal;
import jakarta.annotation.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;

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

    private final StringKeyGenerator tokenGenerator = new Base64StringKeyGenerator(
            Base64.getUrlEncoder().withoutPadding(), 96);

    private final JwtEncoder jwtEncoder;

    protected CommonAuthenticationProvider(TokenProperties tokenProperties, TokenService tokenService
            , JwtEncoder jwtEncoder) {
        this.tokenProperties = tokenProperties;
        this.tokenService = tokenService;
        this.jwtEncoder = jwtEncoder;
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
        String userToken = this.generateKey(authenticationToken.getPrincipal());
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                userToken, Instant.now(), Instant.now().plus(tokenProperties.getUserTokenTimeOut(), ChronoUnit.SECONDS));

        String userRefreshToken;
        if (authenticationToken instanceof RefreshAuthenticationToken refreshAuthenticationToken) {
            userRefreshToken = refreshAuthenticationToken.getRefreshToken();
        } else {
            userRefreshToken = tokenGenerator.generateKey();
        }

        OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(
                userRefreshToken, Instant.now(), Instant.now().plus(tokenProperties.getUserRefreshTokenTimeOut(), ChronoUnit.SECONDS));

        tokenService.save(authenticationToken.getPrincipal(), userToken, userRefreshToken);

        OAuth2AccessTokenAuthenticationToken oAuth2AccessTokenAuthenticationToken =
                new OAuth2AccessTokenAuthenticationToken(
                        registeredClient, authenticationToken, accessToken, refreshToken);
        oAuth2AccessTokenAuthenticationToken.setAuthenticated(true);

        return oAuth2AccessTokenAuthenticationToken;
    }

    @Nullable
    private String generateKey(CorePrincipal principal) {
        if (principal.getType().equals(AuthenticatedType.SYSTEM_USER.getValue())) {
            return tokenGenerator.generateKey();
        } else if (principal.getType().equals(AuthenticatedType.CUSTOMER.getValue())) {
            JwsAlgorithm jwsAlgorithm = SignatureAlgorithm.RS256;
            JwsHeader jwsHeader = JwsHeader.with(jwsAlgorithm)
                    .build();
            Instant issuedAt = Instant.now();
            Instant expiresAt = issuedAt.plus(Duration.ofSeconds(tokenProperties.getAccessTokenTimeOut()));
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .subject(principal.getId().toString())
                    .issuedAt(issuedAt)
                    .expiresAt(expiresAt)
                    .id(UUID.randomUUID().toString())
                    .notBefore(issuedAt)
                    .build();
            Jwt jwt = this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims));
            return jwt.getTokenValue();
        }
        return null;
    }

}
