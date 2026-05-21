package com.wzkris.gateway.remote.api.risk;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.remote.annotation.RemoteInterface;
import com.wzkris.common.remote.constants.ServiceContextPathConstant;
import com.wzkris.common.remote.constants.ServiceIdConstant;
import com.wzkris.gateway.remote.api.risk.request.RiskPassExchangeRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@RemoteInterface(
        serviceId = ServiceIdConstant.CAPTCHA,
        path = ServiceContextPathConstant.RISK
)
@HttpExchange(url = "/risk-captcha-remote")
public interface IRiskCaptchaRemote {

    @PostExchange("/validate-exchange")
    Result<Boolean> validateExchange(@Validated @RequestBody RiskPassExchangeRequest request);

}
