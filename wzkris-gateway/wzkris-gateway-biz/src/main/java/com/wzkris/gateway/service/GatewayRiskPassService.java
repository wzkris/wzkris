package com.wzkris.gateway.service;

import com.wzkris.captcha.enums.CaptchaTypeEnum;
import com.wzkris.captcha.response.RiskPassExchangeResponse;

public interface GatewayRiskPassService {

    RiskPassExchangeResponse grantPass(String clientKey, CaptchaTypeEnum captchaType);

}
