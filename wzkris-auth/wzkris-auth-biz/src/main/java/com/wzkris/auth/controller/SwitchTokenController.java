package com.wzkris.auth.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.wzkris.auth.domain.req.WexcxSwitchReq;
import com.wzkris.auth.service.TokenService;
import com.wzkris.auth.service.impl.LoginTenantService;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.domain.LoginTenant;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.utils.CustomerUtil;
import com.wzkris.principal.httpservice.member.MemberInfoHttpService;
import com.wzkris.principal.httpservice.member.resp.MemberInfoResp;
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
        if (!StringUtil.equals(CustomerUtil.getWxopenid(), identifier)) {
            return Result.requestFail("当前用户不允许切换");
        }

        MemberInfoResp memberInfoResp = memberInfoHttpService.getByWexcxIdentifier(identifier);
        if (memberInfoResp == null) {
            return Result.requestFail("微信未绑定商户账号");
        }

        LoginTenant loginTenant = loginTenantService.buildLoginTenant(memberInfoResp);

        String accessToken = tokenService.generateToken(loginTenant);
        String refreshToken = tokenService.generateToken();

        tokenService.save(loginTenant, accessToken, refreshToken);

        Map<String, Object> parameters = new HashMap();
        parameters.put("access_token", accessToken);
        parameters.put("refresh_token", refreshToken);
        return ok(parameters);
    }

}
