package com.wzkris.message.feign.loginlog;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.message.feign.loginlog.fallback.LoginLogFeignFallback;
import com.wzkris.message.feign.loginlog.req.LoginLogEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 登录日志Feign
 */
@FeignClient(name = ServiceIdConstant.MESSAGE, contextId = "LoginLogFeign",
        fallbackFactory = LoginLogFeignFallback.class,
        path = "/feign-login-log")
public interface LoginLogFeign {

    /**
     * 批量保存登录日志
     */
    @PostMapping("/save")
    void save(@RequestBody List<LoginLogEvent> loginLogEvents);

}

