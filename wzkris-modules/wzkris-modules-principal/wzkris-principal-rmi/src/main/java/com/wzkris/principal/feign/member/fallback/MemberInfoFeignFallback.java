package com.wzkris.principal.feign.member.fallback;

import com.wzkris.principal.feign.admin.req.LoginInfoReq;
import com.wzkris.principal.feign.member.MemberInfoFeign;
import com.wzkris.principal.feign.member.req.QueryMemberPermsReq;
import com.wzkris.principal.feign.member.resp.MemberInfoResp;
import com.wzkris.principal.feign.member.resp.MemberPermissionResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class MemberInfoFeignFallback implements FallbackFactory<MemberInfoFeign> {

    @Override
    public MemberInfoFeign create(Throwable cause) {
        return new MemberInfoFeign() {
            @Override
            public MemberInfoResp getByUsername(String username) {
                log.error("getByUsername => req: {}", username, cause);
                return null;
            }

            @Override
            public MemberInfoResp getByPhoneNumber(String phoneNumber) {
                log.error("getByPhoneNumber => req: {}", phoneNumber, cause);
                return null;
            }

            @Override
            public MemberPermissionResp getPermission(QueryMemberPermsReq memberPermsReq) {
                log.error("getPermission => req: {}", memberPermsReq, cause);
                return null;
            }

            @Override
            public void updateLoginInfo(LoginInfoReq loginInfoReq) {
                log.error("updateLoginInfo => req: {}", loginInfoReq, cause);
            }
        };
    }

}
