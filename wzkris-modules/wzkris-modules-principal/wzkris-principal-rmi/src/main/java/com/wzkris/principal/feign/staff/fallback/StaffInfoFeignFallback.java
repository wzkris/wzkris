package com.wzkris.principal.feign.staff.fallback;

import com.wzkris.principal.feign.admin.req.LoginInfoReq;
import com.wzkris.principal.feign.staff.StaffInfoFeign;
import com.wzkris.principal.feign.staff.req.QueryStaffPermsReq;
import com.wzkris.principal.feign.staff.resp.StaffInfoResp;
import com.wzkris.principal.feign.staff.resp.StaffPermissionResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class StaffInfoFeignFallback implements FallbackFactory<StaffInfoFeign> {

    @Override
    public StaffInfoFeign create(Throwable cause) {
        return new StaffInfoFeign() {
            @Override
            public StaffInfoResp getByUsername(String username) {
                log.error("getByUsername => req: {}", username, cause);
                return null;
            }

            @Override
            public StaffInfoResp getByPhoneNumber(String phoneNumber) {
                log.error("getByPhoneNumber => req: {}", phoneNumber, cause);
                return null;
            }

            @Override
            public StaffPermissionResp getPermission(QueryStaffPermsReq staffPermsReq) {
                log.error("getPermission => req: {}", staffPermsReq, cause);
                return null;
            }

            @Override
            public void updateLoginInfo(LoginInfoReq loginInfoReq) {
                log.error("updateLoginInfo => req: {}", loginInfoReq, cause);
            }
        };
    }

}
