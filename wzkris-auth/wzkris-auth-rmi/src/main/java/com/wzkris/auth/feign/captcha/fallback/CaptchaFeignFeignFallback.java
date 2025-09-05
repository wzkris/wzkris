package com.wzkris.auth.feign.captcha.fallback;

import com.wzkris.auth.feign.captcha.CaptchaFeign;
import com.wzkris.auth.feign.captcha.req.CaptchaCheckReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CaptchaFeignFeignFallback implements FallbackFactory<CaptchaFeign> {

    @Override
    public CaptchaFeign create(Throwable cause) {
        return new CaptchaFeign() {
            @Override
            public boolean validateCaptcha(CaptchaCheckReq captchaCheckReq) {
                log.error("validateCaptcha => req: {}", captchaCheckReq, cause);
                return false;
            }
        };
    }

}
