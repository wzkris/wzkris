package com.wzkris.user.feign.customer.fallback;

import com.wzkris.user.feign.customer.CustomerFeign;
import com.wzkris.user.feign.customer.resp.CustomerResp;
import com.wzkris.user.feign.userinfo.req.LoginInfoReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class CustomerFeignFallback implements FallbackFactory<CustomerFeign> {

    @Override
    public CustomerFeign create(Throwable cause) {
        return new CustomerFeign() {
            @Override
            public CustomerResp getByPhoneNumber(String phoneNumber) {
                log.error("getByPhoneNumber => req: {}", phoneNumber, cause);
                return null;
            }

            @Override
            public CustomerResp getOrRegisterByIdentifier(String identifierType, String authCode) {
                log.error("getOrRegisterByIdentifier => req: {}", identifierType + "," + authCode, cause);
                return null;
            }

            @Override
            public void updateLoginInfo(LoginInfoReq loginInfoReq) {
                log.error("updateLoginInfo => req: {}", loginInfoReq, cause);
            }
        };
    }

}
