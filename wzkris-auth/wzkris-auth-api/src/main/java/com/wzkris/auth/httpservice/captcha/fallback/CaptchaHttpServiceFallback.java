package com.wzkris.auth.httpservice.captcha.fallback;

import com.wzkris.auth.httpservice.captcha.CaptchaHttpService;
import com.wzkris.auth.httpservice.captcha.req.CaptchaCheckReq;
import com.wzkris.common.httpservice.fallback.HttpServiceFallback;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CaptchaHttpServiceFallback implements HttpServiceFallback<CaptchaHttpService> {

    @Override
    public CaptchaHttpService create(Throwable cause) {
        return new CaptchaHttpService() {
            @Override
            public boolean validateCaptcha(CaptchaCheckReq captchaCheckReq) {
                log.error("validateCaptcha => req: {}", captchaCheckReq, cause);
                return false;
            }
        };
    }

}
