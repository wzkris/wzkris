package com.wzkris.usercenter.httpservice.customer.fallback;

import com.wzkris.common.httpservice.fallback.HttpServiceFallback;
import com.wzkris.usercenter.httpservice.admin.req.LoginInfoReq;
import com.wzkris.usercenter.httpservice.customer.CustomerInfoHttpService;
import com.wzkris.usercenter.httpservice.customer.req.WexcxLoginReq;
import com.wzkris.usercenter.httpservice.customer.resp.CustomerResp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerInfoHttpServiceFallback implements HttpServiceFallback<CustomerInfoHttpService> {

    @Override
    public CustomerInfoHttpService create(Throwable cause) {
        return new CustomerInfoHttpService() {
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
