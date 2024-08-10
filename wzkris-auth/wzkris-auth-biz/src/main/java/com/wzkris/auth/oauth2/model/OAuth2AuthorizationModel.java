package com.wzkris.auth.oauth2.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;

import java.util.Map;
import java.util.Set;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class OAuth2AuthorizationModel {
    private String id;
    private String registeredClientId;
    private String principalName;
    private String authorizationGrantType;
    private Set<String> authorizedScopes;
    private Map<Class<? extends OAuth2Token>, OAuth2Authorization.Token<?>> tokens;
    @JsonDeserialize(using = )
    private Map<String, Object> attributes;
}
