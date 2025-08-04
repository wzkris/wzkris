package com.wzkris.auth.rmi.fallback;

import com.wzkris.auth.rmi.TokenFeign;
import com.wzkris.auth.rmi.domain.req.TokenReq;
import com.wzkris.auth.rmi.domain.resp.TokenResponse;
import com.wzkris.common.openfeign.core.FeignLogAggregator;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class TokenFeignFallback implements FallbackFactory<TokenFeign> {

    @Override
    public TokenFeign create(Throwable cause) {
        return new TokenFeign() {
            @Override
            public TokenResponse validateOAuth2(TokenReq tokenReq) {
                FeignLogAggregator.INSTANCE.logPrintError(this.getClass(), cause);
                return TokenResponse.fallback(cause.getMessage());
            }

            @Override
            public TokenResponse validateUser(TokenReq tokenReq) {
                FeignLogAggregator.INSTANCE.logPrintError(this.getClass(), cause);
                return TokenResponse.fallback(cause.getMessage());
            }
        };
    }

}
