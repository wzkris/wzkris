package com.wzkris.user.rmi.fallback;

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
                logPrintError(cause);
                return null;
            }

            @Override
            public SysUserResp getByPhoneNumber(String phoneNumber) {
                logPrintError(cause);
                return null;
            }

            @Override
            public SysPermissionResp getPermission(QueryPermsReq queryPermsReq) {
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
