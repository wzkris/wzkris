package com.wzkris.system.httpservice.loginlog;

import com.wzkris.common.httpservice.annotation.HttpServiceClient;
import com.wzkris.common.httpservice.constants.ServiceIdConstant;
import com.wzkris.system.httpservice.loginlog.fallback.LoginLogHttpServiceFallback;
import com.wzkris.system.httpservice.loginlog.req.LoginLogEvent;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

/**
 * 登录日志Feign
 */
@HttpServiceClient(
        serviceId = ServiceIdConstant.SYSTEM,
        fallbackFactory = LoginLogHttpServiceFallback.class
)
@HttpExchange(url = "/feign-login-log")
public interface LoginLogHttpService {

    /**
     * 批量保存登录日志
     */
    @PostExchange("/save")
    void save(@RequestBody List<LoginLogEvent> loginLogEvents);

}

