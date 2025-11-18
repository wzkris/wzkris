package com.wzkris.message.feign.loginlog.fallback;

import com.wzkris.message.feign.loginlog.LoginLogFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class LoginLogFeignFallback implements FallbackFactory<LoginLogFeign> {

    @Override
    public LoginLogFeign create(Throwable cause) {
        return loginLogEvents -> log.error("save => req: {}", loginLogEvents, cause);
    }

}

