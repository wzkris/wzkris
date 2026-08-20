package com.wzkris.captcha.service;

import com.wzkris.captcha.api.captcha.response.ImageCaptchaDataResponse;

public interface ImageCaptchaService {

    ImageCaptchaDataResponse createCaptcha();

    String redeem(String token, String code);

    Boolean validateToken(String tokenStr);

}
