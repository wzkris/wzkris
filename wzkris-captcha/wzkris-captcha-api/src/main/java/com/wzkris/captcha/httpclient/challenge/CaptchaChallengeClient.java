package com.wzkris.captcha.httpclient.challenge;

import com.wzkris.captcha.httpclient.challenge.req.ValidateChallengeReq;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.httpclient.annotation.HttpClient;
import com.wzkris.common.httpclient.constants.ServiceContextPathConstant;
import com.wzkris.common.httpclient.constants.ServiceIdConstant;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.service.annotation.HttpExchange;

@HttpClient(
        serviceId = ServiceIdConstant.CAPTCHA,
        path = ServiceContextPathConstant.RISK
)
@HttpExchange(url = "/captcha-challenge-client")
public interface CaptchaChallengeClient {

    Result<Boolean> validateChallenge(@Validated ValidateChallengeReq challengeReq);

}
