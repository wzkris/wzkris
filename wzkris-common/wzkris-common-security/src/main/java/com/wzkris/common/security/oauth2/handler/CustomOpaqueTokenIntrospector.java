package com.wzkris.common.security.oauth2.handler;

import com.wzkris.auth.rmi.RmiTokenService;
import com.wzkris.auth.rmi.domain.req.TokenReq;
import com.wzkris.auth.rmi.domain.resp.TokenResponse;
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

    private final RmiTokenService rmiTokenService;

    public CustomOpaqueTokenIntrospector(RmiTokenService rmiTokenService) {
        this.rmiTokenService = rmiTokenService;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        TokenResponse response = rmiTokenService.checkToken(new TokenReq(token));

        if (!response.isSuccess()) {
            throw new OAuth2AuthenticationException(new OAuth2Error(response.getErrorCode()));
        }
        return (AuthBaseUser) response.getPrincipal();
    }
}
