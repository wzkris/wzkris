package com.wzkris.usercenter.remote.interfaces.captcha;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.remote.annotation.RemoteInterface;
import com.wzkris.common.remote.constants.ServiceContextPathConstant;
import com.wzkris.common.remote.constants.ServiceIdConstant;
import com.wzkris.usercenter.remote.interfaces.captcha.request.CaptchaCheckRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.service.annotation.HttpExchange;

@RemoteInterface(
        serviceId = ServiceIdConstant.CAPTCHA,
        path = ServiceContextPathConstant.RISK
)
@HttpExchange(url = "/captcha-remote")
public interface ICaptchaRemote {

    Result<Boolean> check(@Validated CaptchaCheckRequest CaptchaCheckRequest);

}

