package com.wzkris.usercenter.httpclient.oauth2.fallback;

import com.wzkris.common.httpclient.fallback.HttpClientFallback;
import com.wzkris.usercenter.httpclient.oauth2.OAuth2ClientClient;
import com.wzkris.usercenter.httpclient.oauth2.resp.OAuth2ClientResp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuth2ClientHttpClientFallback implements HttpClientFallback<OAuth2ClientClient> {

    @Override
    public OAuth2ClientClient create(Throwable cause) {
        return new OAuth2ClientClient() {
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
