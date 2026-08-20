package com.wzkris.captcha.impl.captcha;

import com.wzkris.captcha.api.captcha.CaptchaApi;
import com.wzkris.captcha.api.captcha.request.CaptchaDispatchRequest;
import com.wzkris.captcha.api.captcha.request.CaptchaImageRequest;
import com.wzkris.captcha.api.captcha.request.CaptchaSlideRequest;
import com.wzkris.captcha.api.captcha.request.RedeemChallengeRequest;
import com.wzkris.captcha.api.captcha.response.CaptchaDispatchResponse;
import com.wzkris.captcha.enums.CaptchaTypeEnum;
import com.wzkris.captcha.service.ChallengeService;
import com.wzkris.captcha.service.ImageCaptchaService;
import com.wzkris.captcha.service.SlideCaptchaService;
import com.wzkris.common.core.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CaptchaApiImpl implements CaptchaApi {

    private final ImageCaptchaService imageCaptchaService;

    private final SlideCaptchaService slideCaptchaService;

    private final ChallengeService challengeService;

    @Override
    public CaptchaDispatchResponse dispatch(CaptchaDispatchRequest request) {
        if (MapUtils.isNotEmpty(request.getCaptcha())) {
            return CaptchaDispatchResponse.of(
                    request.getType(),
                    CaptchaDispatchResponse.PHASE_REDEEM,
                    redeem(request.getType(), request.getCaptcha()));
        }
        return CaptchaDispatchResponse.of(
                request.getType(),
                CaptchaDispatchResponse.PHASE_ISSUE,
                issue(request.getType()));
    }

    private Object issue(CaptchaTypeEnum typeEnum) {
        return switch (typeEnum) {
            case IMAGE -> imageCaptchaService.createCaptcha();
            case SLIDE -> slideCaptchaService.createCaptcha();
            case CHALLENGE -> challengeService.createChallenge();
        };
    }

    private Object redeem(CaptchaTypeEnum typeEnum, Map<String, Object> captcha) {
        return switch (typeEnum) {
            case IMAGE -> {
                CaptchaImageRequest r = JsonUtil.convertValue(captcha, CaptchaImageRequest.class);
                yield imageCaptchaService.redeem(r.getToken(), r.getCode());
            }
            case SLIDE -> {
                CaptchaSlideRequest r = JsonUtil.convertValue(captcha, CaptchaSlideRequest.class);
                yield slideCaptchaService.redeem(r.getToken(), r.getX());
            }
            case CHALLENGE -> {
                RedeemChallengeRequest r = JsonUtil.convertValue(captcha, RedeemChallengeRequest.class);
                yield challengeService.redeem(r.getToken(), r.getSolutions());
            }
        };
    }

}
