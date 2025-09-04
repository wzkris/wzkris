package com.wzkris.user.rmi.fallback;

import com.wzkris.user.rmi.UserInfoFeign;
import com.wzkris.user.rmi.domain.req.LoginInfoReq;
import com.wzkris.user.rmi.domain.req.QueryPermsReq;
import com.wzkris.user.rmi.domain.resp.PermissionResp;
import com.wzkris.user.rmi.domain.resp.UserInfoResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserInfoFeignFallback implements FallbackFactory<UserInfoFeign> {

    @Override
    public UserInfoFeign create(Throwable cause) {
        return new UserInfoFeign() {
            @Override
            public UserInfoResp getByUsername(String username) {
                log.error("getByUsername => req: {}", username, cause);
                return null;
            }

            @Override
            public UserInfoResp getByPhoneNumber(String phoneNumber) {
                log.error("getByPhoneNumber => req: {}", phoneNumber, cause);
                return null;
            }

            @Override
            public PermissionResp getPermission(QueryPermsReq queryPermsReq) {
                log.error("getPermission => req: {}", queryPermsReq, cause);
                return null;
            }

            @Override
            public void updateLoginInfo(LoginInfoReq loginInfoReq) {
                log.error("updateLoginInfo => req: {}", loginInfoReq, cause);
            }
        };
    }

}
