package com.wzkris.captcha.service.impl;

import com.wzkris.captcha.domain.SlideCaptchaInfo;
import com.wzkris.captcha.properties.SlideCaptchaProperties;
import com.wzkris.captcha.response.SlideCaptchaDataResponse;
import com.wzkris.captcha.service.SlideCaptchaService;
import com.wzkris.captcha.store.SlideCaptchaStore;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class SlideCaptchaServiceImpl implements SlideCaptchaService {

    private final SlideCaptchaProperties captchaProperties;

    private final SlideCaptchaStore slideCaptchaStore;

    @Override
    public SlideCaptchaDataResponse createCaptcha() {
        String token = UUID.randomUUID().toString();
        int targetX = RandomUtils.nextInt(100, 300);
        OffsetDateTime expires = OffsetDateTime.now().plus(captchaProperties.getCaptchaExpiresMs(), ChronoUnit.MILLIS);

        SlideCaptchaInfo captchaInfo = new SlideCaptchaInfo(targetX, expires);
        slideCaptchaStore.putCaptcha(token, captchaInfo);

        // 暂时使用模拟图片，实际应该使用真实的图片生成库
        SlideCaptchaDataResponse captchaData = new SlideCaptchaDataResponse();
        captchaData.setToken(token);
        captchaData.setBackgroundImage("mock_background_image");
        captchaData.setSliderImage("mock_slider_image");
        captchaData.setExpires(expires);

        return captchaData;
    }

    @Override
    public String redeem(String token, Integer x) {
        OffsetDateTime now = OffsetDateTime.now();
        SlideCaptchaInfo captchaInfo = slideCaptchaStore.removeCaptcha(token);
        if (Objects.isNull(captchaInfo) || !captchaInfo.getExpires().isAfter(now)) {
            throw new IllegalArgumentException(CaptchaVerificationTokens.CAPTCHA_ERROR);
        }

        int tolerance = captchaProperties.getTolerance();
        if (x < captchaInfo.getTargetX() - tolerance || x > captchaInfo.getTargetX() + tolerance) {
            throw new IllegalArgumentException(CaptchaVerificationTokens.CAPTCHA_ERROR);
        }

        return CaptchaVerificationTokens.issue(
                        captchaProperties.getIdSize(),
                        captchaProperties.getTokenExpiresMs(),
                        now,
                        slideCaptchaStore::putToken)
                .token();
    }

    @Override
    public Boolean validateToken(String tokenStr) {
        return CaptchaVerificationTokens.validate(tokenStr, slideCaptchaStore::removeToken);
    }

}
