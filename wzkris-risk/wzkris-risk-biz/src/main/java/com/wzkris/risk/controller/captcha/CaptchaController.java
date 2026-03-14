package com.wzkris.risk.interfaces.captcha;

import com.wzkris.common.core.model.Result;
import com.wzkris.risk.domain.dto.ChallengeData;
import com.wzkris.risk.domain.req.RedeemChallengeReq;
import com.wzkris.risk.domain.resp.RedeemChallengeResp;
import com.wzkris.risk.httpclient.captcha.req.CaptchaSmsCodeReq;
import com.wzkris.risk.httpclient.captcha.req.CaptchaSmsValidateReq;
import com.wzkris.risk.service.captcha.CaptchaFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "风控验证码")
@Validated
@RestController
@RequestMapping("/risk/captcha")
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaFacadeService captchaFacadeService;

    @Operation(summary = "获取挑战")
    @PostMapping("/challenge")
    public ChallengeData challenge() {
        return captchaFacadeService.challenge();
    }

    @Operation(summary = "验证挑战")
    @PostMapping("/redeem")
    public RedeemChallengeResp redeem(@RequestBody @Valid RedeemChallengeReq request) {
        return captchaFacadeService.redeem(request);
    }

    @Operation(summary = "短信验证码")
    @PostMapping("/smscode")
    public Result<Integer> sendSms(@RequestBody @Valid CaptchaSmsCodeReq req) {
        return captchaFacadeService.sendSms(req);
    }

    @Operation(summary = "校验短信验证码")
    @PostMapping("/validate")
    public Result<Boolean> validateSms(@RequestBody @Valid CaptchaSmsValidateReq req) {
        return captchaFacadeService.validateSms(req);
    }

}
