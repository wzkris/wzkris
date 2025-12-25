package com.wzkris.usercenter.httpservice.member.fallback;

import com.wzkris.common.httpservice.fallback.HttpServiceFallback;
import com.wzkris.usercenter.httpservice.admin.req.LoginInfoReq;
import com.wzkris.usercenter.httpservice.member.MemberInfoHttpService;
import com.wzkris.usercenter.httpservice.member.req.QueryMemberPermsReq;
import com.wzkris.usercenter.httpservice.member.resp.MemberInfoResp;
import com.wzkris.usercenter.httpservice.member.resp.MemberPermissionResp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberInfoHttpServiceFallback implements HttpServiceFallback<MemberInfoHttpService> {

    @Override
    public MemberInfoHttpService create(Throwable cause) {
        return new MemberInfoHttpService() {
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
            public MemberInfoResp getByWexcxIdentifier(String xcxIdentifier) {
                log.error("getByWexcxIdentifier => req: {}", xcxIdentifier, cause);
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
