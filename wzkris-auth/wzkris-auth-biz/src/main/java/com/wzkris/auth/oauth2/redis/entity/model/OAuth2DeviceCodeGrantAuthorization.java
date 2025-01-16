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
import lombok.Getter;
import org.springframework.data.redis.core.index.Indexed;

import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Getter
public class OAuth2DeviceCodeGrantAuthorization extends OAuth2AuthorizationGrantAuthorization {

    private final Principal principal;

    private final DeviceCode deviceCode;

    private final UserCode userCode;

    private final Set<String> requestedScopes;

    @Indexed
    private final String deviceState; // Used to correlate the request during the
    // authorization consent flow

    // @fold:on
    public OAuth2DeviceCodeGrantAuthorization(String id, String registeredClientId, String principalName,
                                              Set<String> authorizedScopes, AccessToken accessToken, RefreshToken refreshToken,
                                              Principal principal, DeviceCode deviceCode, UserCode userCode,
                                              Set<String> requestedScopes, String deviceState) {
        super(id, registeredClientId, principalName, authorizedScopes, accessToken, refreshToken);
        this.principal = principal;
        this.deviceCode = deviceCode;
        this.userCode = userCode;
        this.requestedScopes = requestedScopes;
        this.deviceState = deviceState;
    }

    @Override
    public Long getTimeToLive() {
        long maxLiveTime = -1;
        maxLiveTime = Math.max(maxLiveTime,
                deviceCode != null ? ChronoUnit.SECONDS.between(deviceCode.getIssuedAt(), deviceCode.getExpiresAt()) : -1);
        maxLiveTime = Math.max(maxLiveTime,
                userCode != null ? ChronoUnit.SECONDS.between(userCode.getIssuedAt(), userCode.getExpiresAt()) : -1);

        return maxLiveTime;
    }

    public static class DeviceCode extends AbstractToken {

        public DeviceCode(String tokenValue, Instant issuedAt, Instant expiresAt, boolean invalidated) {
            super(tokenValue, issuedAt, expiresAt, invalidated);
        }
    }

    public static class UserCode extends AbstractToken {

        public UserCode(String tokenValue, Instant issuedAt, Instant expiresAt, boolean invalidated) {
            super(tokenValue, issuedAt, expiresAt, invalidated);
        }
    }
    // @fold:off

}
