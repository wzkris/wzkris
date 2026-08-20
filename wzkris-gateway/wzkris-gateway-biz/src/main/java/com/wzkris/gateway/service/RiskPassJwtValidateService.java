package com.wzkris.gateway.service;

public interface RiskPassJwtValidateService {

    boolean isValid(String riskPassToken, String clientKey);

}
