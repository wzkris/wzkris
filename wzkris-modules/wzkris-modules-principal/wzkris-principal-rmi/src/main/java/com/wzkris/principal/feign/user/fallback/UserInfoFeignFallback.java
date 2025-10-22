package com.wzkris.principal.feign.user.fallback;

import com.wzkris.principal.feign.user.UserInfoFeign;
import com.wzkris.principal.feign.user.req.LoginInfoReq;
import com.wzkris.principal.feign.user.req.QueryUserPermsReq;
import com.wzkris.principal.feign.user.resp.UserInfoResp;
import com.wzkris.principal.feign.user.resp.UserPermissionResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
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
            public UserPermissionResp getPermission(QueryUserPermsReq queryUserPermsReq) {
                log.error("getPermission => req: {}", queryUserPermsReq, cause);
                return null;
            }

            @Override
            public void updateLoginInfo(LoginInfoReq loginInfoReq) {
                log.error("updateLoginInfo => req: {}", loginInfoReq, cause);
            }
        };
    }

}
