package com.wzkris.auth.controller;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Tag(name = "OAuth2自定义端点")
@Slf4j
@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuth2Endpoint {
    private static final String REDIRECT_URL = "redirect_url";
    private final BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
    private final OAuth2AuthorizationService oAuth2AuthorizationService;
    private final RegisteredClientRepository registeredClientRepository;

    @Operation(summary = "当前用户登出")
    @RequestMapping("/logout")
    public ModelAndView logout(ModelAndView modelAndView, HttpServletRequest request) {
        String accessToken = bearerTokenResolver.resolve(request);
        OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(accessToken, null);
        if (authorization != null) {
            oAuth2AuthorizationService.remove(authorization);
        }

        // 获取请求参数中是否包含 回调地址
        String redirectUrl = request.getParameter(REDIRECT_URL);
        if (StrUtil.isNotBlank(redirectUrl)) {
            modelAndView.setView(new RedirectView(redirectUrl));
        }
        else {
            modelAndView.setStatus(HttpStatusCode.valueOf(204));
        }

        return modelAndView;
    }

    @GetMapping("/authorization_code_callback")
    public ResponseEntity<?> callback(String code) {
        log.info("code: {} ", code);
        return new ResponseEntity<>(code, HttpStatus.OK);
    }
}
