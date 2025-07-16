package com.wzkris.auth.rmi.fallback;

import com.wzkris.auth.rmi.RmiTokenFeign;
import com.wzkris.auth.rmi.domain.req.TokenReq;
import com.wzkris.auth.rmi.domain.resp.TokenResponse;
import com.wzkris.common.openfeign.core.FeignLogAggregator;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RmiTokenFeignFallback implements FallbackFactory<RmiTokenFeign> {

    @Override
    public RmiTokenFeign create(Throwable cause) {
        return new RmiTokenFeign() {
            @Override
            public TokenResponse checkOAuth2Token(TokenReq tokenReq) {
                FeignLogAggregator.INSTANCE.logPrintError(this.getClass(), cause);
                return TokenResponse.fallback(cause.getMessage());
            }

            @Override
            public TokenResponse checkUserToken(TokenReq tokenReq) {
                FeignLogAggregator.INSTANCE.logPrintError(this.getClass(), cause);
                return TokenResponse.fallback(cause.getMessage());
            }
        };
    }

}
