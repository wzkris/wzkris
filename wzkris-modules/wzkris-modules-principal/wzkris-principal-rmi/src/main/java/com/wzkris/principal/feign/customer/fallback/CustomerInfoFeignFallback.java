package com.wzkris.principal.feign.customer.fallback;

import com.wzkris.principal.feign.admin.req.LoginInfoReq;
import com.wzkris.principal.feign.customer.CustomerInfoFeign;
import com.wzkris.principal.feign.customer.req.WexcxLoginReq;
import com.wzkris.principal.feign.customer.resp.CustomerResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class CustomerInfoFeignFallback implements FallbackFactory<CustomerInfoFeign> {

    @Override
    public CustomerInfoFeign create(Throwable cause) {
        return new CustomerInfoFeign() {
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
