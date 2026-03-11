package com.wzkris.system.httpclient.loginlog;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.httpclient.annotation.HttpClient;
import com.wzkris.common.httpclient.constants.ServiceContextPathConstant;
import com.wzkris.common.httpclient.constants.ServiceIdConstant;
import com.wzkris.system.httpclient.loginlog.req.LoginLogEvent;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

/**
 * 登录日志Feign
 */
@HttpClient(
        serviceId = ServiceIdConstant.SYSTEM,
        path = ServiceContextPathConstant.SYSTEM
)
@HttpExchange(url = "/login-log-client")
public interface LoginLogClient {

    /**
     * 批量保存登录日志
     */
    @PostExchange("/save")
    Result<Void> save(@RequestBody List<LoginLogEvent> loginLogEvents);

}

