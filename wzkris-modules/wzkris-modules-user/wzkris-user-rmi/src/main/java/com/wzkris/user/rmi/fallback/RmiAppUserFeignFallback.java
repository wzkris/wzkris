package com.wzkris.user.rmi.fallback;

import com.wzkris.user.rmi.RmiAppUserFeign;
import com.wzkris.user.rmi.domain.req.LoginInfoReq;
import com.wzkris.user.rmi.domain.resp.AppUserResp;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RmiAppUserFeignFallback implements FallbackFactory<RmiAppUserFeign> {

    @Override
    public RmiAppUserFeign create(Throwable cause) {
        return new RmiAppUserFeign() {
            @Override
            public AppUserResp getByPhoneNumber(String phoneNumber) {
                logPrintError(cause);
                return null;
            }

            @Override
            public AppUserResp getOrRegisterByIdentifier(String identifierType, String authCode) {
                logPrintError(cause);
                return null;
            }

            @Override
            public void updateLoginInfo(LoginInfoReq loginInfoReq) {
                logPrintError(cause);
            }
        };
    }

}
