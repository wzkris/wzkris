package com.wzkris.auth.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
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
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    @Autowired
    @Lazy
    private WxMaService wxMaService;

    @Override
    public Result<?> switchTenantToken(WexcxSwitchRequest request) {
        String identifier;
        try {
            identifier = wxMaService
                    .getUserService()
                    .getSessionInfo(request.getWxCode())
                    .getOpenid();
        } catch (WxErrorException e) {
            log.error("微信小程序换取openid失败", e);
            throw new RuntimeException(e);
        }

        Result<MemberInfoResponse> memberResult = memberInfoRemote.getByWexcxIdentifier(identifier);
        if (!ResultUtil.check(memberResult)) {
            return Result.requestFail("微信未绑定商户账号");
        }
        MemberInfoResponse memberInfoResponse = memberResult.getData();

        BaseLoginUser loginUser = loginTenantUserServiceImpl.buildLoginTenant(memberInfoResponse);

        Result<MemberPermissionResponse> permissionResult = memberInfoRemote.getPermission(
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
        parameters.put("access_token", tokenPair.getAccessToken());
        parameters.put("refresh_token", tokenPair.getRefreshToken());
        return ok(parameters);
    }

}