package com.thingslink.common.security.config.handler;

import com.thingslink.auth.api.RemoteTokenApi;
import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * @author wzkris
 * @description oauth2退出登录处理器
 * @date 2024-04-10
 */
@Slf4j
@AllArgsConstructor
public class SsoLogoutHandler implements LogoutHandler {

    private final BearerTokenResolver bearerTokenResolver;
    private final RemoteTokenApi remoteTokenApi;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, @Nullable Authentication authentication) {
        String accessToken = bearerTokenResolver.resolve(request);
        remoteTokenApi.logoutByToken(accessToken);
    }
}
