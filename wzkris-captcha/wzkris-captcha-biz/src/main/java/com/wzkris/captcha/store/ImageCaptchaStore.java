package com.wzkris.captcha.store;

import com.wzkris.captcha.domain.ImageCaptchaInfo;

import java.util.Date;

public interface ImageCaptchaStore {

    void putCaptcha(String token, ImageCaptchaInfo captchaInfo);

    ImageCaptchaInfo removeCaptcha(String token);

    void putToken(String tokenKey, Date expires);

    Date removeToken(String tokenKey);

}
