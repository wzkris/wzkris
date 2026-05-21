package com.wzkris.captcha.controller;

import com.wzkris.captcha.request.RiskPassExchangeRequest;
import com.wzkris.captcha.response.RiskPassExchangeResponse;
import com.wzkris.captcha.service.impl.RiskPassServiceImpl;
import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "风控通行票")
@Validated
@RestController
@RequestMapping("/risk-pass")
@RequiredArgsConstructor
public class RiskPassController {

    private final RiskPassServiceImpl riskPassService;

    @Operation(summary = "验证码换风控通行 JWT")
    @PostMapping("/exchange")
    public Result<RiskPassExchangeResponse> exchange(
            @RequestBody @Valid RiskPassExchangeRequest request,
            @RequestHeader(CustomHeaderConstants.X_GATEWAY_CLIENT_IP) String clientIp) {
        return riskPassService.exchange(request, clientIp);
    }

}
