package com.demo.oauthclient.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 用户信息控制器
 * 用于获取当前登录用户信息
 *
 * @author wzkris
 */
@Tag(name = "用户")
@RestController
@RequestMapping("/user")
public class UserController {

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            return Map.of("authenticated", false);
        }
        return Map.of(
                "authenticated", true,
                "name", oauth2User.getName(),
                "attributes", oauth2User.getAttributes()
        );
    }
}

