package com.wzkris.captcha.service;

import com.wzkris.captcha.request.RiskPassExchangeRequest;

public interface RiskDecisionService {

    boolean validateExchange(RiskPassExchangeRequest request);

}
