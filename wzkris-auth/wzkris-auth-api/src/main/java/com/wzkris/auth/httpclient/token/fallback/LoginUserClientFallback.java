package com.wzkris.auth.httpclient.token.fallback;

import com.wzkris.auth.httpclient.token.LoginUserClient;
import com.wzkris.auth.httpclient.token.req.LoginUserReq;
import com.wzkris.auth.httpclient.token.req.OAuth2TokenReq;
import com.wzkris.auth.httpclient.token.resp.LoginUserResp;
import com.wzkris.common.httpclient.fallback.HttpClientFallback;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginUserClientFallback implements HttpClientFallback<LoginUserClient> {

    @Override
    public LoginUserClient create(Throwable cause) {
        return new LoginUserClient() {

            @Override
            public LoginUserResp query(LoginUserReq loginUserReq) {
                log.error("query => req: {}", loginUserReq, cause);
                return LoginUserResp.fallback(cause.getMessage());
            }

            @Override
            public LoginUserResp queryByToken(OAuth2TokenReq request) {
                log.error("queryByToken => req: {}", request, cause);
                return LoginUserResp.fallback(cause.getMessage());
            }
        };
    }

}
