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
package com.wzkris.auth.oauth2.redis.entity.model;

import com.wzkris.auth.oauth2.redis.entity.OAuth2AuthorizationGrantAuthorization;
import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import lombok.Getter;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

@Getter
public class OAuth2AuthorizationCodeGrantAuthorization extends OAuth2AuthorizationGrantAuthorization {

    private final Principal principal;

    private final OAuth2AuthorizationRequest authorizationRequest;

    private final AuthorizationCode authorizationCode;

    @Indexed
    private final String state; // Used to correlate the request during the authorization
    // consent flow

    // @fold:on
    public OAuth2AuthorizationCodeGrantAuthorization(
            String id,
            String registeredClientId,
            String principalName,
            Set<String> authorizedScopes,
            AccessToken accessToken,
            RefreshToken refreshToken,
            Principal principal,
            OAuth2AuthorizationRequest authorizationRequest,
            AuthorizationCode authorizationCode,
            String state) {
        super(id, registeredClientId, principalName, authorizedScopes, accessToken, refreshToken);
        this.principal = principal;
        this.authorizationRequest = authorizationRequest;
        this.authorizationCode = authorizationCode;
        this.state = state;
    }

    @TimeToLive
    protected Long getTimeToLive() {
        long maxLiveTime = -1;
        maxLiveTime = Math.max(
                maxLiveTime,
                authorizationCode != null
                        ? ChronoUnit.SECONDS.between(authorizationCode.getIssuedAt(), authorizationCode.getExpiresAt())
                        : -1);

        return maxLiveTime;
    }

    public static class AuthorizationCode extends AbstractToken {

        public AuthorizationCode(String tokenValue, Instant issuedAt, Instant expiresAt, boolean invalidated) {
            super(tokenValue, issuedAt, expiresAt, invalidated);
        }
    }
    // @fold:off

}
