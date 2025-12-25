package com.wzkris.usercenter.httpservice.admin.fallback;

import com.wzkris.common.httpservice.fallback.HttpServiceFallback;
import com.wzkris.usercenter.httpservice.admin.AdminInfoHttpService;
import com.wzkris.usercenter.httpservice.admin.req.LoginInfoReq;
import com.wzkris.usercenter.httpservice.admin.req.QueryAdminPermsReq;
import com.wzkris.usercenter.httpservice.admin.resp.AdminPermissionResp;
import com.wzkris.usercenter.httpservice.admin.resp.adminInfoResp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdminInfoHttpServiceFallback implements HttpServiceFallback<AdminInfoHttpService> {

    @Override
    public AdminInfoHttpService create(Throwable cause) {
        return new AdminInfoHttpService() {
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
