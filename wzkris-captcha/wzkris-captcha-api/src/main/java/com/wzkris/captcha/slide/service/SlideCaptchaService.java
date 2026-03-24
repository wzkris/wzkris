package com.wzkris.captcha.slide.service;

import com.wzkris.captcha.slide.domain.SlideCaptchaData;

public interface SlideCaptchaService {

    SlideCaptchaData createCaptcha();

    String redeem(String token, Integer x);

    Boolean validateToken(String tokenStr);

}
