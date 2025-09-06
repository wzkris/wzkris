package com.wzkris.auth.feign.token.fallback;

import com.wzkris.auth.feign.token.TokenFeign;
import com.wzkris.auth.feign.token.req.TokenReq;
import com.wzkris.auth.feign.token.resp.TokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class TokenFeignFallback implements FallbackFactory<TokenFeign> {

    @Override
    public TokenFeign create(Throwable cause) {
        return new TokenFeign() {
            @Override
            public TokenResponse validateOAuth2(TokenReq tokenReq) {
                log.error("validateOAuth2 => req: {}", tokenReq, cause);
                return TokenResponse.fallback(cause.getMessage());
            }

            @Override
            public TokenResponse validateUser(TokenReq tokenReq) {
                log.error("validateUser => req: {}", tokenReq, cause);
                return TokenResponse.fallback(cause.getMessage());
            }
        };
    }

}
