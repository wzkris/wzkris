package com.wzkris.auth.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.wzkris.common.core.model.Result.ok;

@Tag(name = "token切换控制器")
@Slf4j
@RestController
@RequestMapping("/switching-token")
@RequiredArgsConstructor
public class SwitchTokenController {

    private final IMemberInfoRemote memberInfoRemote;

    private final TokenService tokenService;

    private final LoginTenantUserServiceImpl loginTenantUserServiceImpl;

    @Autowired
    @Lazy
    private WxMaService wxMaService;

    @Operation(summary = "微信小程序-切换到租户Token")
    @PostMapping("/wexcx/to-tenant")
    public Result<?> tenantToken(@RequestBody @Validated WexcxSwitchRequest switchReq) throws WxErrorException {
        String identifier = wxMaService
                .getUserService()
                .getSessionInfo(switchReq.getWxCode())
                .getOpenid();

        Result<MemberInfoResponse> memberResult = memberInfoRemote.getByWexcxIdentifier(identifier);
        if (!ResultUtil.check(memberResult)) {
            return Result.requestFail("微信未绑定商户账号");
        }
        MemberInfoResponse MemberInfoResponse = memberResult.getData();

        BaseLoginUser loginUser = loginTenantUserServiceImpl.buildLoginTenant(MemberInfoResponse);

        // 获取权限信息
        Result<MemberPermissionResponse> permissionResult = memberInfoRemote.getPermission(
                new MemberPermsQueryRequest(MemberInfoResponse.getMemberId(), MemberInfoResponse.getTenantId()));
        if (!ResultUtil.check(permissionResult)) {
            return Result.requestFail(permissionResult != null ? permissionResult.getMessage() : "查询权限失败");
        }
        MemberPermissionResponse permissions = permissionResult.getData();

        // 生成新的sid
        String sid = java.util.UUID.randomUUID().toString();
        String accessToken = tokenService.generateAccessToken(loginUser, sid);
        String refreshToken = tokenService.generateRefreshToken(loginUser, sid);

        java.util.Set<String> perms = permissions.getGrantedAuthority() != null
                ? new java.util.HashSet<>(permissions.getGrantedAuthority())
                : java.util.Collections.emptySet();

        tokenService.save(loginUser, sid, perms);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("access_token", accessToken);
        parameters.put("refresh_token", refreshToken);
        return ok(parameters);
    }

}

