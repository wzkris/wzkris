package com.wzkris.user.rmi.fallback;

import com.wzkris.user.rmi.OAuth2ClientFeign;
import com.wzkris.user.rmi.domain.resp.OAuth2ClientResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuth2ClientFeignFallback implements FallbackFactory<OAuth2ClientFeign> {

    @Override
    public OAuth2ClientFeign create(Throwable cause) {
        return new OAuth2ClientFeign() {
            @Override
            public OAuth2ClientResp getByClientId(String clientid) {
                log.error("getByClientId => req: {}", clientid, cause);
                return null;
            }
        };
    }

}
