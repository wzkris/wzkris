package com.wzkris.auth.impl;

import com.wzkris.auth.api.SwitchTokenApi;
import com.wzkris.auth.domain.TokenPair;
import com.wzkris.auth.remote.interfaces.member.IMemberInfoRemote;
import com.wzkris.auth.remote.interfaces.member.request.MemberPermsQueryRequest;
import com.wzkris.auth.remote.interfaces.member.response.MemberInfoResponse;
import com.wzkris.auth.remote.interfaces.member.response.MemberPermissionResponse;
import com.wzkris.auth.request.WexcxSwitchRequest;
import com.wzkris.auth.service.TokenService;
import com.wzkris.auth.service.impl.LoginTenantUserServiceImpl;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.wzkris.common.core.model.Result.ok;

@Slf4j
@Service
@RequiredArgsConstructor
public class SwitchTokenApiImpl implements SwitchTokenApi {

    private final IMemberInfoRemote memberInfoRemote;

    private final TokenService tokenService;

    private final LoginTenantUserServiceImpl loginTenantUserServiceImpl;

    @Override
    public Result<?> switchTenantToken(WexcxSwitchRequest request) {
        Result<MemberInfoResponse> memberResult = memberInfoRemote.queryByWexcxCode(request.getWxCode());
        if (!ResultUtil.check(memberResult)) {
            return Result.requestFail("微信未绑定商户账号");
        }
        MemberInfoResponse memberInfoResponse = memberResult.getData();

        BaseLoginUser loginUser = loginTenantUserServiceImpl.buildLoginTenant(memberInfoResponse);

        Result<MemberPermissionResponse> permissionResult = memberInfoRemote.queryPermission(
                new MemberPermsQueryRequest(memberInfoResponse.getMemberId(), memberInfoResponse.getTenantId()));
        if (!ResultUtil.check(permissionResult)) {
            return Result.requestFail(permissionResult != null ? permissionResult.getMessage() : "查询权限失败");
        }
        MemberPermissionResponse permissions = permissionResult.getData();

        java.util.Set<String> perms = permissions.getGrantedAuthority() != null
                ? new java.util.HashSet<>(permissions.getGrantedAuthority())
                : java.util.Collections.emptySet();

        TokenPair tokenPair = tokenService.login(loginUser, perms);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(OAuth2ParameterNames.ACCESS_TOKEN, tokenPair.getAccessToken());
        parameters.put(OAuth2ParameterNames.REFRESH_TOKEN, tokenPair.getRefreshToken());
        return ok(parameters);
    }

}