package com.wzkris.common.security.serialize;

import com.fasterxml.jackson.annotation.*;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Instant;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class RegisteredClientMixin {

    @JsonCreator
    public RegisteredClientMixin(@JsonProperty("id") String id,
                                 @JsonProperty("clientId") String clientId,
                                 @JsonProperty("clientIdIssuedAt") Instant clientIdIssuedAt,
                                 @JsonProperty("clientSecret") String clientSecret,
                                 @JsonProperty("clientSecretExpiresAt") Instant clientSecretExpiresAt,
                                 @JsonProperty("clientName") String clientName,
                                 @JsonProperty("clientAuthenticationMethods") Set<ClientAuthenticationMethod> clientAuthenticationMethods,
                                 @JsonProperty("authorizationGrantTypes") Set<AuthorizationGrantType> authorizationGrantTypes,
                                 @JsonProperty("redirectUris") Set<String> redirectUris,
                                 @JsonProperty("postLogoutRedirectUris") Set<String> postLogoutRedirectUris,
                                 @JsonProperty("scopes") Set<String> scopes,
                                 @JsonProperty("clientSettings") ClientSettings clientSettings,
                                 @JsonProperty("tokenSettings") TokenSettings tokenSettings) {
    }
}
