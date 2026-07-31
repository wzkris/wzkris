package com.wzkris.captcha.service.impl;

import com.wzkris.captcha.api.captcha.response.ImageCaptchaDataResponse;
import com.wzkris.captcha.domain.ImageCaptchaInfo;
import com.wzkris.captcha.properties.ImageCaptchaProperties;
import com.wzkris.captcha.service.ImageCaptchaService;
import com.wzkris.captcha.store.ImageCaptchaStore;
import com.wzkris.captcha.utils.CaptchaVerificationTokens;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class ImageCaptchaServiceImpl implements ImageCaptchaService {

    private static final String CODE_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final ImageCaptchaProperties captchaProperties;

    private final ImageCaptchaStore imageCaptchaStore;

    @Override
    public ImageCaptchaDataResponse createCaptcha() {
        String token = UUID.randomUUID().toString();
        String code = RandomStringUtils.secure().next(captchaProperties.getCodeLength(), CODE_CHARS);
        OffsetDateTime expires = OffsetDateTime.now().plus(captchaProperties.getCaptchaExpiresMs(), ChronoUnit.MILLIS);

        ImageCaptchaInfo captchaInfo = new ImageCaptchaInfo(code, expires);
        imageCaptchaStore.putCaptcha(token, captchaInfo);

        // 暂时使用模拟图片，实际应该使用真实的图片生成库
        ImageCaptchaDataResponse captchaData = new ImageCaptchaDataResponse();
        captchaData.setToken(token);
        captchaData.setImage("mock_image_data");
        captchaData.setExpires(expires);

        return captchaData;
    }

    @Override
    public String redeem(String token, String code) {
        OffsetDateTime now = OffsetDateTime.now();
        ImageCaptchaInfo captchaInfo = imageCaptchaStore.removeCaptcha(token);
        if (Objects.isNull(captchaInfo) || !captchaInfo.getExpires().isAfter(now)) {
            throw new IllegalArgumentException(CaptchaVerificationTokens.CAPTCHA_ERROR);
        }

        if (!captchaInfo.getCode().equalsIgnoreCase(code)) {
            throw new IllegalArgumentException(CaptchaVerificationTokens.CAPTCHA_ERROR);
        }

        return CaptchaVerificationTokens.issue(
                        captchaProperties.getIdSize(),
                        captchaProperties.getTokenExpiresMs(),
                        now,
                        imageCaptchaStore::putToken)
                .token();
    }

    @Override
    public Boolean validateToken(String tokenStr) {
        return CaptchaVerificationTokens.validate(tokenStr, imageCaptchaStore::removeToken);
    }

}
