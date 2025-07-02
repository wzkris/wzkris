package com.wzkris.user.rmi.fallback;

import com.wzkris.common.openfeign.core.FeignLogAggregator;
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
                FeignLogAggregator.INSTANCE.logPrintError(this.getClass(), cause);
                return null;
            }

            @Override
            public AppUserResp getOrRegisterByIdentifier(String identifierType, String authCode) {
                FeignLogAggregator.INSTANCE.logPrintError(this.getClass(), cause);
                return null;
            }

            @Override
            public void updateLoginInfo(LoginInfoReq loginInfoReq) {
                FeignLogAggregator.INSTANCE.logPrintError(this.getClass(), cause);
            }
        };
    }

}
