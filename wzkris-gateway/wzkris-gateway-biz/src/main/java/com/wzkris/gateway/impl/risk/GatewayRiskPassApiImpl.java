package com.wzkris.gateway.impl.risk;

import com.wzkris.captcha.request.RiskPassExchangeRequest;
import com.wzkris.captcha.response.RiskPassExchangeResponse;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.ResultUtil;
import com.wzkris.gateway.api.risk.GatewayRiskPassApi;
import com.wzkris.gateway.remote.api.risk.IRiskCaptchaRemote;
import com.wzkris.gateway.service.GatewayRiskPassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GatewayRiskPassApiImpl implements GatewayRiskPassApi {

    private final IRiskCaptchaRemote riskCaptchaRemote;

    private final GatewayRiskPassService gatewayRiskPassService;

    @Override
    public Result<RiskPassExchangeResponse> exchange(RiskPassExchangeRequest request, String clientKey) {
        Result<Boolean> validated = riskCaptchaRemote.validateExchange(request);
        if (!ResultUtil.check(validated) || !Boolean.TRUE.equals(validated.getData())) {
            return Result.requestFail("invalidParameter.captcha.error");
        }
        return Result.ok(gatewayRiskPassService.grantPass(clientKey, request.getCaptchaType()));
    }

}
