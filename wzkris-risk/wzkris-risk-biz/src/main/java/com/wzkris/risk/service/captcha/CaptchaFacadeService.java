package com.wzkris.risk.service.captcha;

import com.wzkris.common.core.model.Result;
import com.wzkris.risk.domain.dto.ChallengeData;
import com.wzkris.risk.domain.req.RedeemChallengeReq;
import com.wzkris.risk.domain.resp.RedeemChallengeResp;
import com.wzkris.risk.httpclient.captcha.req.CaptchaSmsCodeReq;
import com.wzkris.risk.httpclient.captcha.req.CaptchaSmsValidateReq;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaptchaFacadeService {

    private final CaptchaService captchaService;

    public ChallengeData challenge() {
        return captchaService.createChallenge();
    }

    public RedeemChallengeResp redeem(RedeemChallengeReq request) {
        return captchaService.redeem(request);
    }

    public Result<Integer> sendSms(CaptchaSmsCodeReq req) {
        boolean valid = captchaService.validateChallenge(req.getCaptchaId());
        if (!valid) {
            return Result.requestFail("验证码异常");
        }
        captchaService.validateSmsMaxTry(req.getPhone(), 1, 120);
        String code = String.valueOf(RandomUtils.secure().randomInt(100_000, 999_999));
        captchaService.setCaptcha(req.getPhone(), code);
        return Result.ok();
    }

    public Result<Boolean> validateSms(CaptchaSmsValidateReq req) {
        return Result.ok(captchaService.validateSmsCode(req.getPhone(), req.getCode()));
    }

}
