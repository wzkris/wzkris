package com.wzkris.user.rmi.fallback;

import com.wzkris.common.openfeign.core.FeignLogAggregator;
import com.wzkris.user.rmi.domain.resp.OAuth2ClientResp;
import com.wzkris.user.rmi.OAuth2ClientFeign;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class OAuth2ClientFeignFallback implements FallbackFactory<OAuth2ClientFeign> {

    @Override
    public OAuth2ClientFeign create(Throwable cause) {
        return new OAuth2ClientFeign() {
            @Override
            public OAuth2ClientResp getByClientId(String clientid) {
                FeignLogAggregator.INSTANCE.logPrintError(this.getClass(), cause);
                return null;
            }
        };
    }

}
