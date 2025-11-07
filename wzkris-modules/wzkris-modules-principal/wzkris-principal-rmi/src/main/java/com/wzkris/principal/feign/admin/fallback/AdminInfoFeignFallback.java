package com.wzkris.principal.feign.admin.fallback;

import com.wzkris.principal.feign.admin.AdminInfoFeign;
import com.wzkris.principal.feign.admin.req.LoginInfoReq;
import com.wzkris.principal.feign.admin.req.QueryAdminPermsReq;
import com.wzkris.principal.feign.admin.resp.AdminPermissionResp;
import com.wzkris.principal.feign.admin.resp.adminInfoResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class AdminInfoFeignFallback implements FallbackFactory<AdminInfoFeign> {

    @Override
    public AdminInfoFeign create(Throwable cause) {
        return new AdminInfoFeign() {
            @Override
            public adminInfoResp getByUsername(String username) {
                log.error("getByUsername => req: {}", username, cause);
                return null;
            }

            @Override
            public adminInfoResp getByPhoneNumber(String phoneNumber) {
                log.error("getByPhoneNumber => req: {}", phoneNumber, cause);
                return null;
            }

            @Override
            public AdminPermissionResp getPermission(QueryAdminPermsReq queryAdminPermsReq) {
                log.error("getPermission => req: {}", queryAdminPermsReq, cause);
                return null;
            }

            @Override
            public void updateLoginInfo(LoginInfoReq loginInfoReq) {
                log.error("updateLoginInfo => req: {}", loginInfoReq, cause);
            }
        };
    }

}
