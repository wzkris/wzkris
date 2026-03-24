package com.wzkris.captcha.image.service;

import com.wzkris.captcha.image.domain.ImageCaptchaData;

public interface ImageCaptchaService {

    ImageCaptchaData createCaptcha();

    String redeem(String token, String code);

    Boolean validateToken(String tokenStr);

}
