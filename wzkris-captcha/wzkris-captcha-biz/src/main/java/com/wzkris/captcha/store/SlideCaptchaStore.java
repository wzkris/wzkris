package com.wzkris.captcha.store;

import com.wzkris.captcha.domain.SlideCaptchaInfo;

import java.time.OffsetDateTime;

public interface SlideCaptchaStore {

    void putCaptcha(String token, SlideCaptchaInfo captchaInfo);

    SlideCaptchaInfo removeCaptcha(String token);

    void putToken(String tokenKey, OffsetDateTime expires);

    OffsetDateTime removeToken(String tokenKey);

}
