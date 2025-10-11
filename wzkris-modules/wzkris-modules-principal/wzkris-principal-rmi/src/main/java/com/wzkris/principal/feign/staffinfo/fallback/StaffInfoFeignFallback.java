package com.wzkris.principal.feign.staffinfo.fallback;

import com.wzkris.principal.feign.staffinfo.StaffInfoFeign;
import com.wzkris.principal.feign.staffinfo.req.QueryStaffPermsReq;
import com.wzkris.principal.feign.staffinfo.resp.StaffInfoResp;
import com.wzkris.principal.feign.staffinfo.resp.StaffPermissionResp;
import com.wzkris.principal.feign.userinfo.req.LoginInfoReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class StaffInfoFeignFallback implements FallbackFactory<StaffInfoFeign> {

    @Override
    public StaffInfoFeign create(Throwable cause) {
        return new StaffInfoFeign() {
            @Override
            public StaffInfoResp getByStaffName(String staffName) {
                log.error("getByStaffName => req: {}", staffName, cause);
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
