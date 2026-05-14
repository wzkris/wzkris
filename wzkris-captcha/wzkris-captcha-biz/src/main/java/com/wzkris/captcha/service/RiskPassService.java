package com.wzkris.captcha.service;

import com.wzkris.captcha.request.RiskPassExchangeRequest;
import com.wzkris.captcha.response.RiskPassExchangeResponse;
import com.wzkris.common.core.model.Result;

public interface RiskPassService {

    Result<RiskPassExchangeResponse> exchange(RiskPassExchangeRequest request);

}
