package com.wzkris.user.rmi.fallback;

import com.wzkris.user.rmi.CustomerFeign;
import com.wzkris.user.rmi.domain.req.LoginInfoReq;
import com.wzkris.user.rmi.domain.resp.CustomerResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
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
