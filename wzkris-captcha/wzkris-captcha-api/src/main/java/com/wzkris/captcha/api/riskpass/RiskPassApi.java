package com.wzkris.captcha.api.riskpass;

import com.wzkris.captcha.api.riskpass.request.RiskPassExchangeRequest;
import com.wzkris.captcha.api.riskpass.response.RiskPassExchangeResponse;
import com.wzkris.common.core.model.Result;

public interface RiskPassApi {

    Result<RiskPassExchangeResponse> exchange(RiskPassExchangeRequest request, String clientKey);

}
