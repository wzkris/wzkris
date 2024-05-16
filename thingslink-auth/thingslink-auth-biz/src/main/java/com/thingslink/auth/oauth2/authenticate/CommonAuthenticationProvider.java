package com.thingslink.auth.oauth2.authenticate;

import com.thingslink.common.security.oauth2.model.OAuth2User;
import com.thingslink.common.security.utils.OAuth2ExceptionUtil;
import jakarta.annotation.Nonnull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description provider基类，验证Authentication
 */
public abstract class CommonAuthenticationProvider<T extends CommonAuthenticationToken> implements AuthenticationProvider {

    protected static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1";
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    private final OAuth2AuthorizationService authorizationService;

    protected CommonAuthenticationProvider(OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                           OAuth2AuthorizationService authorizationService) {
        this.tokenGenerator = tokenGenerator;
        this.authorizationService = authorizationService;
    }

    /**
     * 校验客户端模式和scope
     */
    @Nonnull
    public Set<String> checkClient(CommonAuthenticationToken authenticationToken, RegisteredClient registeredClient) {
        if (registeredClient == null) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.UNSUPPORTED_GRANT_TYPE, "oauth2.client.invalid");
        }

        if (!registeredClient.getAuthorizationGrantTypes().contains(authenticationToken.getGrantType())) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.UNSUPPORTED_GRANT_TYPE, "oauth2.unsupport.granttype");
        }

        Set<String> authorizedScopes = new LinkedHashSet<>();
        // 校验 scope
        if (!authenticationToken.getScopes().isEmpty()) {
            for (String requestedScope : authenticationToken.getScopes()) {
                if (!registeredClient.getScopes().contains(requestedScope)) {
                    OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_SCOPE, "oauth2.scope.invalid");
                }
            }
            authorizedScopes.addAll(registeredClient.getScopes());
        }
        return authorizedScopes;
    }

    /**
     * 认证核心方法
     */
    protected abstract OAuth2AuthenticationToken doAuthenticate(Authentication authentication);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        T customAuthentication = (T) authentication;

        OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(customAuthentication);


        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        // 校验客户端
        Set<String> authorizedScopes = this.checkClient(customAuthentication, registeredClient);

        // 验证并拿到授权
        OAuth2AuthenticationToken oAuth2AuthenticationToken = this.doAuthenticate(customAuthentication);

        // @formatter:off
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                    .registeredClient(registeredClient)
                    .principal(oAuth2AuthenticationToken)
                    .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                    .authorizedScopes(authorizedScopes)
                    .authorizationGrantType(customAuthentication.getGrantType())
                    .authorizationGrant(customAuthentication);

        // @formatter:on
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization
                .withRegisteredClient(registeredClient)
                .principalName(oAuth2AuthenticationToken.getName())
                .authorizationGrantType(customAuthentication.getGrantType())
                .authorizedScopes(authorizedScopes)
                .attribute(Principal.class.getName(), oAuth2AuthenticationToken);

        // ----- Access token -----
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2ExceptionUtil.throwError(OAuth2ErrorCodes.SERVER_ERROR, "The token generator failed to generate the access_token.");
        }

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(accessToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims()));
        }
        else {
            authorizationBuilder.accessToken(accessToken);
        }

        // ----- Refresh token -----
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                // Do not issue refresh token to public client
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                OAuth2ExceptionUtil.throwError(OAuth2ErrorCodes.SERVER_ERROR, "The token generator failed to generate the refresh_token.");
            }

            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);
        }

        // ----- ID token -----
        OidcIdToken idToken;
        if (authorizedScopes.contains(OidcScopes.OPENID)) {
            // @formatter:off
                tokenContext = tokenContextBuilder
                        .tokenType(new OAuth2TokenType(OidcParameterNames.ID_TOKEN))
                        .authorization(authorizationBuilder.build())	// ID token customizer may need access to the access token and/or refresh token
                        .build();
                // @formatter:on
            OAuth2Token generatedIdToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedIdToken instanceof Jwt)) {
                OAuth2ExceptionUtil.throwError(OAuth2ErrorCodes.SERVER_ERROR, "The token generator failed to generate the id_token.");
            }

            idToken = new OidcIdToken(generatedIdToken.getTokenValue(), generatedIdToken.getIssuedAt(),
                    generatedIdToken.getExpiresAt(), ((Jwt) generatedIdToken).getClaims());
            authorizationBuilder.token(idToken, (metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, idToken.getClaims()));
        }
        else {
            idToken = null;
        }

        OAuth2Authorization authorization = authorizationBuilder.build();
        this.authorizationService.save(authorization);

        Map<String, Object> additionalParameters = new HashMap<>(2);
        if (idToken != null) {
            additionalParameters.put(OidcParameterNames.ID_TOKEN, idToken.getTokenValue());
        }

        // 传递用户信息供OAuth2后续流程使用
        additionalParameters.put(OAuth2User.class.getName(), oAuth2AuthenticationToken.getPrincipal());

        OAuth2AccessTokenAuthenticationToken oAuth2AccessTokenAuthenticationToken =
                new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken, additionalParameters);
        oAuth2AccessTokenAuthenticationToken.setAuthenticated(true);
        return oAuth2AccessTokenAuthenticationToken;
    }

    public static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }

        throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_CLIENT, null, ERROR_URI));
    }

}
