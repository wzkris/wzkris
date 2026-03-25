package com.wzkris.captcha.store;

import com.wzkris.captcha.domain.SlideCaptchaInfo;

import java.util.Date;

public interface SlideCaptchaStore {

    void putCaptcha(String token, SlideCaptchaInfo captchaInfo);

    SlideCaptchaInfo removeCaptcha(String token);

    void putToken(String tokenKey, Date expires);

    Date removeToken(String tokenKey);

}
