package com.wzkris.auth.controller;

import cn.hutool.core.util.StrUtil;
import com.wzkris.common.core.utils.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Tag(name = "OAuth自定义端点")
@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class OAuth2Endpoint {

    private static final String REDIRECT_URL = "redirect_url";

    private final BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    @Operation(summary = "用户登出")
    @RequestMapping("/oauth/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String accessToken = bearerTokenResolver.resolve(request);
        OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(accessToken, null);
        if (authorization != null) {
            oAuth2AuthorizationService.remove(authorization);
        }

        // 获取请求参数中是否包含 回调地址
        String redirectUrl = request.getParameter(REDIRECT_URL);
        if (StrUtil.isNotBlank(redirectUrl) && StringUtil.ishttp(redirectUrl)) {
            response.sendRedirect(redirectUrl);
        } else {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
    }

    @ResponseBody
    @GetMapping("/authorization_code_callback")
    public ResponseEntity<?> callback(String code) {
        log.info("code: {} ", code);
        return new ResponseEntity<>(code, HttpStatus.OK);
    }
}
