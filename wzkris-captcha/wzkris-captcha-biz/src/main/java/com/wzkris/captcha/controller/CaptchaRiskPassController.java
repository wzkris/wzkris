package com.wzkris.captcha.controller;

import com.wzkris.captcha.request.RiskPassExchangeRequest;
import com.wzkris.captcha.response.RiskPassExchangeResponse;
import com.wzkris.captcha.service.RiskPassService;
import com.wzkris.common.core.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "验证码")
@Validated
@RestController
@RequestMapping("/risk-pass")
@RequiredArgsConstructor
public class CaptchaRiskPassController {

    private final RiskPassService riskPassService;

    @Operation(summary = "验证码通过后换发网关风控通行票")
    @PostMapping("/exchange")
    public Result<RiskPassExchangeResponse> exchange(@RequestBody @Valid RiskPassExchangeRequest request) {
        return riskPassService.exchange(request);
    }

}
