package com.wzkris.auth.remote.loginlog;

import com.wzkris.auth.remote.loginlog.req.LoginLogEvent;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.remote.annotation.RemoteInterface;
import com.wzkris.common.remote.constants.ServiceContextPathConstant;
import com.wzkris.common.remote.constants.ServiceIdConstant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

/**
 * 登录日志Feign
 */
@RemoteInterface(
        serviceId = ServiceIdConstant.SYSTEM,
        path = ServiceContextPathConstant.SYSTEM
)
@HttpExchange(url = "/login-log-client")
public interface ILoginLogRemote {

    /**
     * 批量保存登录日志
     */
    @PostExchange("/save")
    Result<Void> save(@RequestBody List<LoginLogEvent> loginLogEvents);

}
