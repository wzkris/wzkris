package com.wzkris.usercenter.httpservice.oauth2.fallback;

import com.wzkris.common.httpservice.fallback.HttpServiceFallback;
import com.wzkris.usercenter.httpservice.oauth2.OAuth2ClientHttpService;
import com.wzkris.usercenter.httpservice.oauth2.resp.OAuth2ClientResp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuth2ClientHttpServiceFallback implements HttpServiceFallback<OAuth2ClientHttpService> {

    @Override
    public OAuth2ClientHttpService create(Throwable cause) {
        return new OAuth2ClientHttpService() {
            @Override
            public OAuth2ClientResp getById(String id) {
                log.error("getById => req: {}", id, cause);
                return null;
            }

            @Override
            public OAuth2ClientResp getByClientId(String clientid) {
                log.error("getByClientId => req: {}", clientid, cause);
                return null;
            }
        };
    }

}
