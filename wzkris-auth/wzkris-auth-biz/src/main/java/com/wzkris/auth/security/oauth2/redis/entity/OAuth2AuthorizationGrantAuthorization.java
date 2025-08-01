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
package com.wzkris.auth.security.oauth2.redis.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;

@Getter
@RedisHash("oauth2_authorization")
public abstract class OAuth2AuthorizationGrantAuthorization {

    @Id
    private final String id;

    private final String registeredClientId;

    private final String principalName;

    private final Set<String> authorizedScopes;

    private final AccessToken accessToken;

    private final RefreshToken refreshToken;

    // @fold:on
    protected OAuth2AuthorizationGrantAuthorization(
            String id,
            String registeredClientId,
            String principalName,
            Set<String> authorizedScopes,
            AccessToken accessToken,
            RefreshToken refreshToken) {
        this.id = id;
        this.registeredClientId = registeredClientId;
        this.principalName = principalName;
        this.authorizedScopes = authorizedScopes;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @TimeToLive
    protected Long getTimeToLive() {
        long maxLiveTime = -1;
        maxLiveTime = Math.max(
                maxLiveTime,
                accessToken != null
                        ? ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt())
                        : -1);
        maxLiveTime = Math.max(
                maxLiveTime,
                refreshToken != null
                        ? ChronoUnit.SECONDS.between(refreshToken.getIssuedAt(), refreshToken.getExpiresAt())
                        : -1);

        return maxLiveTime;
    }

    @Getter
    protected abstract static class AbstractToken {

        @Indexed
        private final String tokenValue;

        private final Instant issuedAt;

        private final Instant expiresAt;

        private final boolean invalidated;

        protected AbstractToken(String tokenValue, Instant issuedAt, Instant expiresAt, boolean invalidated) {
            this.tokenValue = tokenValue;
            this.issuedAt = issuedAt;
            this.expiresAt = expiresAt;
            this.invalidated = invalidated;
        }

    }

    @Getter
    public static class ClaimsHolder {

        private final Map<String, Object> claims;

        public ClaimsHolder(Map<String, Object> claims) {
            this.claims = claims;
        }

    }

    @Getter
    public static class AccessToken extends AbstractToken {

        private final OAuth2AccessToken.TokenType tokenType;

        private final Set<String> scopes;

        private final OAuth2TokenFormat tokenFormat;

        private final ClaimsHolder claims;

        public AccessToken(
                String tokenValue,
                Instant issuedAt,
                Instant expiresAt,
                boolean invalidated,
                OAuth2AccessToken.TokenType tokenType,
                Set<String> scopes,
                OAuth2TokenFormat tokenFormat,
                ClaimsHolder claims) {
            super(tokenValue, issuedAt, expiresAt, invalidated);
            this.tokenType = tokenType;
            this.scopes = scopes;
            this.tokenFormat = tokenFormat;
            this.claims = claims;
        }

    }

    public static class RefreshToken extends AbstractToken {

        public RefreshToken(String tokenValue, Instant issuedAt, Instant expiresAt, boolean invalidated) {
            super(tokenValue, issuedAt, expiresAt, invalidated);
        }

    }
    // @fold:off

}
