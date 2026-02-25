package com.wzkris.usercenter.httpclient.member.fallback;

import com.wzkris.common.httpclient.fallback.HttpClientFallback;
import com.wzkris.usercenter.httpclient.admin.req.LoginInfoReq;
import com.wzkris.usercenter.httpclient.member.MemberInfoClient;
import com.wzkris.usercenter.httpclient.member.req.QueryMemberPermsReq;
import com.wzkris.usercenter.httpclient.member.resp.MemberInfoResp;
import com.wzkris.usercenter.httpclient.member.resp.MemberPermissionResp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberInfoClientFallback implements HttpClientFallback<MemberInfoClient> {

    @Override
    public MemberInfoClient create(Throwable cause) {
        return new MemberInfoClient() {
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
