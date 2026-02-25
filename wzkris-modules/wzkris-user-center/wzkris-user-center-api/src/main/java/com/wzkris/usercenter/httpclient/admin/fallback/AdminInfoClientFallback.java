package com.wzkris.usercenter.httpclient.admin.fallback;

import com.wzkris.common.httpclient.fallback.HttpClientFallback;
import com.wzkris.usercenter.httpclient.admin.AdminInfoClient;
import com.wzkris.usercenter.httpclient.admin.req.LoginInfoReq;
import com.wzkris.usercenter.httpclient.admin.req.QueryAdminPermsReq;
import com.wzkris.usercenter.httpclient.admin.resp.AdminInfoResp;
import com.wzkris.usercenter.httpclient.admin.resp.AdminPermissionResp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdminInfoClientFallback implements HttpClientFallback<AdminInfoClient> {

    @Override
    public AdminInfoClient create(Throwable cause) {
        return new AdminInfoClient() {
            @Override
            public AdminInfoResp getByUsername(String username) {
                log.error("getByUsername => req: {}", username, cause);
                return null;
            }

            @Override
            public AdminInfoResp getByPhoneNumber(String phoneNumber) {
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
