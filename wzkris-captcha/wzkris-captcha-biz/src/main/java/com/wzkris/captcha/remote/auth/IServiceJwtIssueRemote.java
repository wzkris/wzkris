package com.wzkris.captcha.remote.auth;

import com.wzkris.auth.remote.api.jwt.request.ServiceJwtIssueRequest;
import com.wzkris.auth.remote.api.jwt.response.ServiceJwtIssueResponse;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.remote.annotation.RemoteInterface;
import com.wzkris.common.remote.constants.ServiceContextPathConstant;
import com.wzkris.common.remote.constants.ServiceIdConstant;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@RemoteInterface(
        serviceId = ServiceIdConstant.AUTH,
        path = ServiceContextPathConstant.AUTH
)
@HttpExchange(url = "/service-jwt-remote")
public interface IServiceJwtIssueRemote {

    @PostExchange("/issue")
    Result<ServiceJwtIssueResponse> issue(@Validated @RequestBody ServiceJwtIssueRequest request);

}
