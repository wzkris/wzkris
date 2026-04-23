package com.wzkris.captcha.store;

import com.wzkris.captcha.domain.ImageCaptchaInfo;

import java.time.OffsetDateTime;

public interface ImageCaptchaStore {

    void putCaptcha(String token, ImageCaptchaInfo captchaInfo);

    ImageCaptchaInfo removeCaptcha(String token);

    void putToken(String tokenKey, OffsetDateTime expires);

    OffsetDateTime removeToken(String tokenKey);

}
