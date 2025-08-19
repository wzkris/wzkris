package com.wzkris.user.rmi.fallback;

import com.wzkris.user.rmi.AppUserFeign;
import com.wzkris.user.rmi.domain.req.LoginInfoReq;
import com.wzkris.user.rmi.domain.resp.AppUserResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppUserFeignFallback implements FallbackFactory<AppUserFeign> {

    @Override
    public AppUserFeign create(Throwable cause) {
        return new AppUserFeign() {
            @Override
            public AppUserResp getByPhoneNumber(String phoneNumber) {
                log.error("getByPhoneNumber => req: {}", phoneNumber, cause);
                return null;
            }

            @Override
            public AppUserResp getOrRegisterByIdentifier(String identifierType, String authCode) {
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
