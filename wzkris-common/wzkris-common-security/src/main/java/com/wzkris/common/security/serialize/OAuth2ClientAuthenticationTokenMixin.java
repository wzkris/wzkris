package com.wzkris.common.security.serialize;

import com.fasterxml.jackson.annotation.*;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class OAuth2ClientAuthenticationTokenMixin {

    @JsonCreator
    public OAuth2ClientAuthenticationTokenMixin(@JsonProperty("clientId") String clientId,
                                                @JsonProperty("clientAuthenticationMethod") ClientAuthenticationMethod clientAuthenticationMethod,
                                                @JsonProperty("credentials") @Nullable Object credentials,
                                                @JsonProperty("additionalParameters") @Nullable Map<String, Object> additionalParameters) {
    }
}
