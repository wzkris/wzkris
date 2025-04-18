/*
 * Copyright 2020-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wzkris.auth.oauth2.redis.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

@Getter
@RedisHash("oauth2_registered_client")
public class OAuth2RegisteredClient {

    @Id
    private final String id;

    @Indexed
    private final String clientId;

    private final Instant clientIdIssuedAt;

    private final String clientSecret;

    private final Instant clientSecretExpiresAt;

    private final String clientName;

    private final Set<ClientAuthenticationMethod> clientAuthenticationMethods;

    private final Set<AuthorizationGrantType> authorizationGrantTypes;

    private final Set<String> redirectUris;

    private final Set<String> postLogoutRedirectUris;

    private final Set<String> scopes;

    private final ClientSettings clientSettings;

    private final TokenSettings tokenSettings;

    // @fold:on
    public OAuth2RegisteredClient(String id, String clientId, Instant clientIdIssuedAt, String clientSecret,
                                  Instant clientSecretExpiresAt, String clientName,
                                  Set<ClientAuthenticationMethod> clientAuthenticationMethods,
                                  Set<AuthorizationGrantType> authorizationGrantTypes, Set<String> redirectUris,
                                  Set<String> postLogoutRedirectUris, Set<String> scopes, ClientSettings clientSettings,
                                  TokenSettings tokenSettings) {
        this.id = id;
        this.clientId = clientId;
        this.clientIdIssuedAt = clientIdIssuedAt;
        this.clientSecret = clientSecret;
        this.clientSecretExpiresAt = clientSecretExpiresAt;
        this.clientName = clientName;
        this.clientAuthenticationMethods = clientAuthenticationMethods;
        this.authorizationGrantTypes = authorizationGrantTypes;
        this.redirectUris = redirectUris;
        this.postLogoutRedirectUris = postLogoutRedirectUris;
        this.scopes = scopes;
        this.clientSettings = clientSettings;
        this.tokenSettings = tokenSettings;
    }

    @Getter
    public static class ClientSettings {

        private final boolean requireProofKey;

        private final boolean requireAuthorizationConsent;

        private final String jwkSetUrl;

        private final JwsAlgorithm tokenEndpointAuthenticationSigningAlgorithm;

        private final String x509CertificateSubjectDN;

        public ClientSettings(boolean requireProofKey, boolean requireAuthorizationConsent, String jwkSetUrl,
                              JwsAlgorithm tokenEndpointAuthenticationSigningAlgorithm, String x509CertificateSubjectDN) {
            this.requireProofKey = requireProofKey;
            this.requireAuthorizationConsent = requireAuthorizationConsent;
            this.jwkSetUrl = jwkSetUrl;
            this.tokenEndpointAuthenticationSigningAlgorithm = tokenEndpointAuthenticationSigningAlgorithm;
            this.x509CertificateSubjectDN = x509CertificateSubjectDN;
        }
    }

    @Getter
    public static class TokenSettings {

        private final Duration authorizationCodeTimeToLive;

        private final Duration accessTokenTimeToLive;

        private final OAuth2TokenFormat accessTokenFormat;

        private final Duration deviceCodeTimeToLive;

        private final boolean reuseRefreshTokens;

        private final Duration refreshTokenTimeToLive;

        private final SignatureAlgorithm idTokenSignatureAlgorithm;

        private final boolean x509CertificateBoundAccessTokens;

        public TokenSettings(Duration authorizationCodeTimeToLive, Duration accessTokenTimeToLive,
                             OAuth2TokenFormat accessTokenFormat, Duration deviceCodeTimeToLive, boolean reuseRefreshTokens,
                             Duration refreshTokenTimeToLive, SignatureAlgorithm idTokenSignatureAlgorithm,
                             boolean x509CertificateBoundAccessTokens) {
            this.authorizationCodeTimeToLive = authorizationCodeTimeToLive;
            this.accessTokenTimeToLive = accessTokenTimeToLive;
            this.accessTokenFormat = accessTokenFormat;
            this.deviceCodeTimeToLive = deviceCodeTimeToLive;
            this.reuseRefreshTokens = reuseRefreshTokens;
            this.refreshTokenTimeToLive = refreshTokenTimeToLive;
            this.idTokenSignatureAlgorithm = idTokenSignatureAlgorithm;
            this.x509CertificateBoundAccessTokens = x509CertificateBoundAccessTokens;
        }
    }
    // @fold:off

}
