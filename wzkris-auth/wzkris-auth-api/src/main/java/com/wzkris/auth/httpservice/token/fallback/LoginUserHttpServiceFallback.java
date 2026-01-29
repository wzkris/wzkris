package com.wzkris.auth.httpservice.token.fallback;

import com.wzkris.auth.httpservice.token.LoginUserHttpService;
import com.wzkris.auth.httpservice.token.req.LoginUserReq;
import com.wzkris.auth.httpservice.token.resp.LoginUserResp;
import com.wzkris.common.httpservice.fallback.HttpServiceFallback;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginUserHttpServiceFallback implements HttpServiceFallback<LoginUserHttpService> {

    @Override
    public LoginUserHttpService create(Throwable cause) {
        return new LoginUserHttpService() {

            @Override
            public LoginUserResp query(LoginUserReq loginUserReq) {
                log.error("query => req: {}", loginUserReq, cause);
                return LoginUserResp.fallback(cause.getMessage());
            }
        };
    }

}

