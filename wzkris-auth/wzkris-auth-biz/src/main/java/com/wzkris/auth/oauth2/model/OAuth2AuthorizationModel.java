package com.wzkris.auth.oauth2.model;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;

import java.util.Map;
import java.util.Set;

@Data
@Accessors(chain = true)
@FieldNameConstants
public class OAuth2AuthorizationModel {
    private String id;
    private String registeredClientId;
    private String principalName;
    private String authorizationGrantType;
    private Set<String> authorizedScopes;
    private Map<Class<? extends OAuth2Token>, OAuth2Authorization.Token<?>> tokens;
    private Map<String, Object> attributes;
}
