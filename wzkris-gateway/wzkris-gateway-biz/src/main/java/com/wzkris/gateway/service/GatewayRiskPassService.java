package com.wzkris.gateway.service;

import com.wzkris.gateway.api.risk.response.RiskPassExchangeResponse;

public interface GatewayRiskPassService {

    RiskPassExchangeResponse grantPass(String clientKey, String captchaType);

}
