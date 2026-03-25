package com.wzkris.captcha.service;

import com.wzkris.captcha.response.SlideCaptchaDataResponse;

public interface SlideCaptchaService {

    SlideCaptchaDataResponse createCaptcha();

    String redeem(String token, Integer x);

    Boolean validateToken(String tokenStr);

}
