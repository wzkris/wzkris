package com.wzkris.captcha.slide.store;

import com.wzkris.captcha.slide.domain.SlideCaptchaInfo;

import java.util.Date;

public interface SlideCaptchaStore {

    void putCaptcha(String token, SlideCaptchaInfo captchaInfo);

    SlideCaptchaInfo removeCaptcha(String token);

    void putToken(String tokenKey, Date expires);

    Date removeToken(String tokenKey);

}