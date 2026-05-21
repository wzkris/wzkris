package com.wzkris.gateway.api.risk;

import com.wzkris.common.core.model.Result;
import com.wzkris.gateway.api.risk.response.RiskPassExchangeResponse;
import com.wzkris.gateway.remote.api.risk.request.RiskPassExchangeRequest;

public interface GatewayRiskPassApi {

    Result<RiskPassExchangeResponse> exchange(RiskPassExchangeRequest request, String clientKey);

}
