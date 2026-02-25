package com.wzkris.auth.httpclient.captcha.fallback;

import com.wzkris.auth.httpclient.captcha.CaptchaClient;
import com.wzkris.auth.httpclient.captcha.req.CaptchaCheckReq;
import com.wzkris.common.httpclient.fallback.HttpClientFallback;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CaptchaClientFallback implements HttpClientFallback<CaptchaClient> {

    @Override
    public CaptchaClient create(Throwable cause) {
        return new CaptchaClient() {
            @Override
            public boolean validateCaptcha(CaptchaCheckReq captchaCheckReq) {
                log.error("validateCaptcha => req: {}", captchaCheckReq, cause);
                return false;
            }
        };
    }

}
