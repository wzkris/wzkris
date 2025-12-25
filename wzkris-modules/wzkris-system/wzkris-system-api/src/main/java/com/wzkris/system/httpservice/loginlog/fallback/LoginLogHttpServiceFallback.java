package com.wzkris.system.httpservice.loginlog.fallback;

import com.wzkris.common.httpservice.fallback.HttpServiceFallback;
import com.wzkris.system.httpservice.loginlog.LoginLogHttpService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginLogHttpServiceFallback implements HttpServiceFallback<LoginLogHttpService> {

    @Override
    public LoginLogHttpService create(Throwable cause) {
        return loginLogEvents -> log.error("save => req: {}", loginLogEvents, cause);
    }

}

