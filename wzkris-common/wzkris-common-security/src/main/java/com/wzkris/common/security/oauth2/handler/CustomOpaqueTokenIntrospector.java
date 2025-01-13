package com.wzkris.common.security.oauth2.handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzkris.auth.api.RemoteTokenApi;
import com.wzkris.auth.api.domain.request.TokenReq;
import com.wzkris.auth.api.domain.response.TokenResponse;
import com.wzkris.common.security.oauth2.domain.AuthBaseUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 自定义token自省
 * @date : 2024/3/8 14:34.
 * @UPDATE: 2024/5/14 10:11 custom from @org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector
 */
@Slf4j
public final class CustomOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RemoteTokenApi remoteTokenApi;

    public CustomOpaqueTokenIntrospector(RemoteTokenApi remoteTokenApi) {
        this.remoteTokenApi = remoteTokenApi;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        TokenResponse response = remoteTokenApi.checkToken(new TokenReq(token));

        if (!response.isSuccess()) {
            throw new OAuth2AuthenticationException(new OAuth2Error(response.getErrorCode()));
        }
        return this.adaptToCustomResponse(response.getPrincipal());
    }

    private AuthBaseUser adaptToCustomResponse(Object responseEntity) {
        return objectMapper.convertValue(responseEntity, AuthBaseUser.class);
    }
}
