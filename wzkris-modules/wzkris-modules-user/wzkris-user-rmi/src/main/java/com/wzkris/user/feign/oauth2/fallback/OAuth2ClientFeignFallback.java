package com.wzkris.user.feign.oauth2.fallback;

import com.wzkris.user.feign.oauth2.OAuth2ClientFeign;
import com.wzkris.user.feign.oauth2.resp.OAuth2ClientResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
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
