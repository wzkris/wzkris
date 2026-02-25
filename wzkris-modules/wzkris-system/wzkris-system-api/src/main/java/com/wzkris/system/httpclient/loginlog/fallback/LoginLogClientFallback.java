package com.wzkris.system.httpclient.loginlog.fallback;

import com.wzkris.common.httpclient.fallback.HttpClientFallback;
import com.wzkris.system.httpclient.loginlog.LoginLogClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginLogClientFallback implements HttpClientFallback<LoginLogClient> {

    @Override
    public LoginLogClient create(Throwable cause) {
        return loginLogEvents -> log.error("save => req: {}", loginLogEvents, cause);
    }

}

