package com.wzkris.common.security.oauth2.introspector;

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
 * @description : 支持opaque_token和jwt_token
 * @date : 2025/06/20 9:10.
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
