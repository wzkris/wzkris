package com.wzkris.auth.api.fallback;

import com.wzkris.auth.api.RemoteTokenApi;
import com.wzkris.auth.api.domain.request.TokenReq;
import com.wzkris.auth.api.domain.response.TokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteTokenApiFallback implements FallbackFactory<RemoteTokenApi> {
    @Override
    public RemoteTokenApi create(Throwable cause) {
        return new RemoteTokenApi() {
            @Override
            public TokenResponse checkToken(TokenReq tokenReq) {
                log.error("验证token发生异常，errMsg：{}", cause.getMessage());

                return TokenResponse.unavailable(cause.getMessage());
            }
        };
    }

}
