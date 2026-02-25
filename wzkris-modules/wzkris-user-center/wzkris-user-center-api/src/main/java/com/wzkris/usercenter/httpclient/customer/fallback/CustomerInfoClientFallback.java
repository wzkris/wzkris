package com.wzkris.usercenter.httpclient.customer.fallback;

import com.wzkris.common.httpclient.fallback.HttpClientFallback;
import com.wzkris.usercenter.httpclient.admin.req.LoginInfoReq;
import com.wzkris.usercenter.httpclient.customer.CustomerInfoClient;
import com.wzkris.usercenter.httpclient.customer.req.WexcxLoginReq;
import com.wzkris.usercenter.httpclient.customer.resp.CustomerResp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerInfoClientFallback implements HttpClientFallback<CustomerInfoClient> {

    @Override
    public CustomerInfoClient create(Throwable cause) {
        return new CustomerInfoClient() {
            @Override
            public CustomerResp getByPhoneNumber(String phoneNumber) {
                log.error("getByPhoneNumber => req: {}", phoneNumber, cause);
                return null;
            }

            @Override
            public CustomerResp wexcxLogin(WexcxLoginReq req) {
                log.error("wexcxLogin => req: {}", req, cause);
                return null;
            }

            @Override
            public void updateLoginInfo(LoginInfoReq loginInfoReq) {
                log.error("updateLoginInfo => req: {}", loginInfoReq, cause);
            }
        };
    }

}
