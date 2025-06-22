package com.wzkris.auth.rmi.fallback;

import com.wzkris.auth.rmi.RmiCaptchaFeign;
import com.wzkris.auth.rmi.domain.req.SmsCheckReq;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RmiCaptchaFeignFeignFallback implements FallbackFactory<RmiCaptchaFeign> {

    @Override
    public RmiCaptchaFeign create(Throwable cause) {
        return new RmiCaptchaFeign() {
            @Override
            public boolean validateSms(SmsCheckReq smsCheckReq) {
                logPrintError(cause);
                return false;
            }
        };
    }

}
