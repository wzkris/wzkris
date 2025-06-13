package com.wzkris.user.rmi.fallback;

import com.wzkris.user.rmi.RmiOAuth2ClientFeign;
import com.wzkris.user.rmi.domain.resp.OAuth2ClientResp;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RmiOAuth2ClientFeignFallback implements FallbackFactory<RmiOAuth2ClientFeign> {

    @Override
    public RmiOAuth2ClientFeign create(Throwable cause) {
        return new RmiOAuth2ClientFeign() {
            @Override
            public OAuth2ClientResp getByClientId(String clientid) {
                logPrintError(cause);
                return null;
            }
        };
    }

}
