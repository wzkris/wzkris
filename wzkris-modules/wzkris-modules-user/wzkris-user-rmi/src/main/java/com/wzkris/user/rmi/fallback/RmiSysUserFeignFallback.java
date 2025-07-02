package com.wzkris.user.rmi.fallback;

import com.wzkris.common.openfeign.core.FeignLogAggregator;
import com.wzkris.user.rmi.RmiSysUserFeign;
import com.wzkris.user.rmi.domain.req.LoginInfoReq;
import com.wzkris.user.rmi.domain.req.QueryPermsReq;
import com.wzkris.user.rmi.domain.resp.SysPermissionResp;
import com.wzkris.user.rmi.domain.resp.SysUserResp;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RmiSysUserFeignFallback implements FallbackFactory<RmiSysUserFeign> {

    @Override
    public RmiSysUserFeign create(Throwable cause) {
        return new RmiSysUserFeign() {
            @Override
            public SysUserResp getByUsername(String username) {
                FeignLogAggregator.INSTANCE.logPrintError(this.getClass(), cause);
                return null;
            }

            @Override
            public SysUserResp getByPhoneNumber(String phoneNumber) {
                FeignLogAggregator.INSTANCE.logPrintError(this.getClass(), cause);
                return null;
            }

            @Override
            public SysPermissionResp getPermission(QueryPermsReq queryPermsReq) {
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
