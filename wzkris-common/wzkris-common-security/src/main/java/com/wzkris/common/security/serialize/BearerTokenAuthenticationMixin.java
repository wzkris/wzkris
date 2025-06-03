package com.wzkris.common.security.serialize;

import com.fasterxml.jackson.annotation.*;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class BearerTokenAuthenticationMixin {

    @JsonCreator
    public BearerTokenAuthenticationMixin(
            @JsonProperty("principal") OAuth2AuthenticatedPrincipal principal,
            @JsonProperty("credentials") OAuth2AccessToken credentials,
            @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities) {}
}
