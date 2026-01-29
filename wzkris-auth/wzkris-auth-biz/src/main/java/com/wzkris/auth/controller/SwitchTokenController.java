package com.wzkris.auth.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.wzkris.auth.domain.req.WexcxSwitchReq;
import com.wzkris.auth.service.TokenService;
import com.wzkris.auth.service.impl.LoginTenantService;
import com.wzkris.common.core.model.LoginUser;
import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.httpservice.member.MemberInfoHttpService;
import com.wzkris.usercenter.httpservice.member.req.QueryMemberPermsReq;
import com.wzkris.usercenter.httpservice.member.resp.MemberInfoResp;
import com.wzkris.usercenter.httpservice.member.resp.MemberPermissionResp;
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

    private final MemberInfoHttpService memberInfoHttpService;

    private final TokenService tokenService;

    private final LoginTenantService loginTenantService;

    @Autowired
    @Lazy
    private WxMaService wxMaService;

    @Operation(summary = "微信小程序-切换到租户Token")
    @PostMapping("/wexcx/to-tenant")
    public Result<?> tenantToken(@RequestBody @Validated WexcxSwitchReq switchReq) throws WxErrorException {
        String identifier = wxMaService
                .getUserService()
                .getSessionInfo(switchReq.getWxCode())
                .getOpenid();

        MemberInfoResp memberInfoResp = memberInfoHttpService.getByWexcxIdentifier(identifier);
        if (memberInfoResp == null) {
            return Result.requestFail("微信未绑定商户账号");
        }

        LoginUser loginUser = loginTenantService.buildLoginTenant(memberInfoResp);

        // 获取权限信息
        MemberPermissionResp permissions = memberInfoHttpService.getPermission(
                new QueryMemberPermsReq(memberInfoResp.getMemberId(), memberInfoResp.getTenantId()));

        String accessToken = tokenService.generateAccessToken(loginUser);
        String refreshToken = tokenService.generateRefreshToken();

        java.util.Set<String> perms = permissions.getGrantedAuthority() != null
                ? new java.util.HashSet<>(permissions.getGrantedAuthority())
                : java.util.Collections.emptySet();

        tokenService.save(loginUser, refreshToken, perms);

        Map<String, Object> parameters = new HashMap();
        parameters.put("access_token", accessToken);
        parameters.put("refresh_token", refreshToken);
        return ok(parameters);
    }

}
