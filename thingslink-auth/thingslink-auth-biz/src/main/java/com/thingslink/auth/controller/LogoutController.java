package com.thingslink.auth.controller;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 登出控制器
 */
@Controller
@AllArgsConstructor
public class LogoutController {
    private static final String REDIRECT_URL = "redirect_url";
    private final BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    @RequestMapping("/oauth2/logout")
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
}