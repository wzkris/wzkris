package com.wzkris.gateway.controller;

import com.wzkris.captcha.request.RiskPassExchangeRequest;
import com.wzkris.captcha.response.RiskPassExchangeResponse;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.ResultUtil;
import com.wzkris.gateway.remote.api.risk.IRiskCaptchaRemote;
import com.wzkris.gateway.service.GatewayRiskPassService;
import com.wzkris.gateway.utils.GatewayRiskClientKeys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "网关风控")
@Validated
@RestController
@RequestMapping("/risk-pass")
@RequiredArgsConstructor
public class RiskPassController {

    private final IRiskCaptchaRemote riskCaptchaRemote;

    private final GatewayRiskPassService gatewayRiskPassService;

    @Operation(summary = "验证码通过后换发风控通行票")
    @PostMapping("/exchange")
    public Result<RiskPassExchangeResponse> exchange(
            @RequestBody @Valid RiskPassExchangeRequest request, HttpServletRequest httpRequest) {
        Result<Boolean> validated = riskCaptchaRemote.validateExchange(request);
        if (!ResultUtil.check(validated) || !Boolean.TRUE.equals(validated.getData())) {
            return Result.requestFail("invalidParameter.captcha.error");
        }
        String clientKey = GatewayRiskClientKeys.defaultCompositeKey(httpRequest);
        return Result.ok(gatewayRiskPassService.grantPass(clientKey, request.getCaptchaType()));
    }

}
