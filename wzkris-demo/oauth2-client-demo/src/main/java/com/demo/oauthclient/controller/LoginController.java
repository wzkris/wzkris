package com.demo.oauthclient.controller;

import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 登录控制器
 * 直接重定向到 OAuth2 授权端点，避免使用默认登录页面（需要静态资源）
 *
 * @author wzkris
 */
@Controller
public class LoginController {

    /**
     * 登录页面 - 直接重定向到 OAuth2 授权端点
     * {@link OAuth2AuthorizationRequestRedirectFilter}
     */
    @GetMapping("/login")
    public String login() {
        return "redirect:/oauth2/authorization/auth-center";
    }

}

