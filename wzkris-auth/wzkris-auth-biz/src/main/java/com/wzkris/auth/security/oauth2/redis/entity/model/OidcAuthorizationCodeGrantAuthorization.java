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
package com.wzkris.auth.security.oauth2.redis.entity.model;

import lombok.Getter;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Getter
public class OidcAuthorizationCodeGrantAuthorization extends OAuth2AuthorizationCodeGrantAuthorization {

    private final IdToken idToken;

    // @fold:on
    public OidcAuthorizationCodeGrantAuthorization(
            String id,
            String registeredClientId,
            String principalName,
            Set<String> authorizedScopes,
            AccessToken accessToken,
            RefreshToken refreshToken,
            Principal principal,
            OAuth2AuthorizationRequest authorizationRequest,
            AuthorizationCode authorizationCode,
            String state,
            IdToken idToken) {
        super(
                id,
                registeredClientId,
                principalName,
                authorizedScopes,
                accessToken,
                refreshToken,
                principal,
                authorizationRequest,
                authorizationCode,
                state);
        this.idToken = idToken;
    }

    @TimeToLive
    protected Long getTimeToLive() {
        long maxLiveTime = -1;
        maxLiveTime = Math.max(
                maxLiveTime,
                idToken != null ? ChronoUnit.SECONDS.between(idToken.getIssuedAt(), idToken.getExpiresAt()) : -1);

        return maxLiveTime;
    }

    @Getter
    public static class IdToken extends AbstractToken {

        private final ClaimsHolder claims;

        public IdToken(
                String tokenValue, Instant issuedAt, Instant expiresAt, boolean invalidated, ClaimsHolder claims) {
            super(tokenValue, issuedAt, expiresAt, invalidated);
            this.claims = claims;
        }

    }
    // @fold:off

}
