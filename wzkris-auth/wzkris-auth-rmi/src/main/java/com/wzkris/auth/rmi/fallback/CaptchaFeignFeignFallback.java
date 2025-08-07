package com.wzkris.auth.rmi.fallback;

import com.wzkris.auth.rmi.CaptchaFeign;
import com.wzkris.auth.rmi.domain.req.CaptchaCheckReq;
import com.wzkris.common.openfeign.core.FeignLogAggregator;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class CaptchaFeignFeignFallback implements FallbackFactory<CaptchaFeign> {

    @Override
    public CaptchaFeign create(Throwable cause) {
        return new CaptchaFeign() {
            @Override
            public boolean validateCaptcha(CaptchaCheckReq captchaCheckReq) {
                FeignLogAggregator.INSTANCE.logPrintError(this.getClass(), cause);
                return false;
            }
        };
    }

}
