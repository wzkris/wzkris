package com.wzkris.common.security.oauth2.filter;

import com.wzkris.auth.rmi.RmiTokenFeign;
import com.wzkris.auth.rmi.domain.req.TokenReq;
import com.wzkris.auth.rmi.domain.resp.TokenResponse;
import com.wzkris.common.security.domain.AuthedClient;
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
public final class OAuth2OpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final RmiTokenFeign rmiTokenFeign;

    public OAuth2OpaqueTokenIntrospector(RmiTokenFeign rmiTokenFeign) {
        this.rmiTokenFeign = rmiTokenFeign;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        TokenResponse response = rmiTokenFeign.checkOAuth2Token(new TokenReq(token));

        if (!response.isSuccess()) {
            throw new OAuth2AuthenticationException(new OAuth2Error(response.getErrorCode()));
        }

        return (AuthedClient) response.getPrincipal();
    }

}
