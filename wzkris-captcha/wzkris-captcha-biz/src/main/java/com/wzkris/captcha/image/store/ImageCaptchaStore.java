package com.wzkris.captcha.image.store;

import com.wzkris.captcha.image.domain.ImageCaptchaInfo;

import java.util.Date;

public interface ImageCaptchaStore {

    void putCaptcha(String token, ImageCaptchaInfo captchaInfo);

    ImageCaptchaInfo removeCaptcha(String token);

    void putToken(String tokenKey, Date expires);

    Date removeToken(String tokenKey);

}
