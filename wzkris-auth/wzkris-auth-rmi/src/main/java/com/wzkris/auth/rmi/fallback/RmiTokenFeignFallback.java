package com.wzkris.auth.rmi.fallback;

import com.wzkris.auth.rmi.RmiTokenFeign;
import com.wzkris.auth.rmi.domain.req.TokenReq;
import com.wzkris.auth.rmi.domain.resp.TokenResponse;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RmiTokenFeignFallback implements FallbackFactory<RmiTokenFeign> {

    @Override
    public RmiTokenFeign create(Throwable cause) {
        return new RmiTokenFeign() {
            @Override
            public TokenResponse checkOAuth2Token(TokenReq tokenReq) {
                logPrintError(cause);
                return TokenResponse.okAnonymous();
            }

            @Override
            public TokenResponse checkUserToken(TokenReq tokenReq) {
                logPrintError(cause);
                return TokenResponse.okAnonymous();
            }
        };
    }

}
