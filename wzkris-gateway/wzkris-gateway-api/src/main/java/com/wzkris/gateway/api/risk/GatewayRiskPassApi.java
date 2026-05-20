package com.wzkris.gateway.api.risk;

import com.wzkris.captcha.request.RiskPassExchangeRequest;
import com.wzkris.captcha.response.RiskPassExchangeResponse;
import com.wzkris.common.core.model.Result;

public interface GatewayRiskPassApi {

    Result<RiskPassExchangeResponse> exchange(RiskPassExchangeRequest request, String clientKey);

}
