package com.wzkris.auth.controller;

import cn.hutool.core.util.StrUtil;
import com.wzkris.auth.domain.resp.AppUserinfo;
import com.wzkris.auth.domain.resp.SysUserinfo;
import com.wzkris.auth.oauth2.redis.entity.OAuth2AuthorizationGrantAuthorization;
import com.wzkris.auth.oauth2.redis.repository.OAuth2AuthorizationGrantAuthorizationRepository;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.oauth2.domain.model.ClientUser;
import com.wzkris.common.security.oauth2.resolver.CustomBearerTokenResolver;
import com.wzkris.common.security.utils.ClientUserUtil;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.common.web.model.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "OAuth自定义端点")
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class OAuth2Endpoint extends BaseController {

    private static final String REDIRECT_URL = "redirect_url";

    private final BearerTokenResolver bearerTokenResolver = new CustomBearerTokenResolver();

    private final OAuth2AuthorizationGrantAuthorizationRepository auth2AuthorizationGrantAuthorizationRepository;

    @Operation(summary = "用户信息")
    @GetMapping("/sysuserinfo")
    public Result<SysUserinfo> loginUser() {
        SysUserinfo userinfo = new SysUserinfo();
        userinfo.setAdmin(LoginUserUtil.isAdmin());
        userinfo.setSuperTenant(LoginUserUtil.isSuperTenant());
        userinfo.setUsername(LoginUserUtil.getUsername());
        userinfo.setAuthorities(LoginUserUtil.getAuthorities());
        return ok(userinfo);
    }

    @Operation(summary = "用户信息")
    @GetMapping("/appuserinfo")
    public Result<AppUserinfo> clientUser() {
        ClientUser clientUser = ClientUserUtil.getClientUser();
        AppUserinfo userinfo = BeanUtil.convert(clientUser, AppUserinfo.class);
        return ok(userinfo);
    }

    @Operation(summary = "用户登出")
    @PostMapping("/oauth/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String resolvedToken = bearerTokenResolver.resolve(request);
        if (StringUtil.isBlank(resolvedToken)) {
            return;
        }

        OAuth2AuthorizationGrantAuthorization grantAuthorization = auth2AuthorizationGrantAuthorizationRepository
                .findByAccessToken_TokenValue(resolvedToken);
        if (grantAuthorization != null) {
            auth2AuthorizationGrantAuthorizationRepository.delete(grantAuthorization);
        }

        // 获取请求参数中是否包含 回调地址
        String redirectUrl = request.getParameter(REDIRECT_URL);
        if (StrUtil.isNotBlank(redirectUrl) && StringUtil.ishttp(redirectUrl)) {
            response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
            response.sendRedirect(redirectUrl);
        } else {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
    }

    @GetMapping("/authorization_code_callback")
    public ResponseEntity<?> callback(String code) {
        log.info("code: {} ", code);
        return new ResponseEntity<>(code, HttpStatus.OK);
    }
}
