package com.wzkris.auth.feign.token.fallback;

import com.wzkris.auth.feign.token.TokenHttpService;
import com.wzkris.auth.feign.token.req.TokenReq;
import com.wzkris.auth.feign.token.resp.TokenResponse;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.core.model.domain.LoginClient;
import com.wzkris.common.httpservice.fallback.HttpServiceFallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenHttpServiceFallbackFactory implements HttpServiceFallbackFactory<TokenHttpService> {

    @Override
    public TokenHttpService create(Throwable cause) {
        return new TokenHttpService() {
            @Override
            public TokenResponse<LoginClient> validateClient(TokenReq tokenReq) {
                log.error("validateClient => req: {}", tokenReq, cause);
                return TokenResponse.fallback(cause.getMessage());
            }

            @Override
            public TokenResponse<MyPrincipal> validatePrincipal(TokenReq tokenReq) {
                log.error("validatePrincipal => req: {}", tokenReq, cause);
                return TokenResponse.fallback(cause.getMessage());
            }
        };
    }

}

