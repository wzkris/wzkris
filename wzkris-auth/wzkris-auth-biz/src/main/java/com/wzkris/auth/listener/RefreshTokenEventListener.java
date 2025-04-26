package com.wzkris.auth.listener;

import com.wzkris.auth.config.TokenProperties;
import com.wzkris.auth.domain.OnlineUser;
import com.wzkris.auth.listener.event.RefreshTokenEvent;
import com.wzkris.auth.utils.OnlineUserUtil;
import com.wzkris.common.security.oauth2.domain.AuthBaseUser;
import com.wzkris.common.security.oauth2.domain.model.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.time.Duration;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 登录事件监听
 * @date : 2023/8/28 10:05
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenEventListener {

    private final TokenProperties tokenProperties;

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    @Async
    @EventListener
    public void refreshTokenEvent(RefreshTokenEvent event) {
        OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(event.getRefreshToken(), OAuth2TokenType.REFRESH_TOKEN);

        final AuthBaseUser baseUser = (AuthBaseUser) ((UsernamePasswordAuthenticationToken) authorization.getAttribute(Principal.class.getName())).getPrincipal();

        switch (baseUser.getLoginType()) {
            case SYSTEM_USER -> {
                LoginUser loginUser = (LoginUser) baseUser;
                RMapCache<String, OnlineUser> onlineCache = OnlineUserUtil.getOnlineCache(loginUser.getUserId());
                onlineCache.expireEntry(authorization.getId(), Duration.ofSeconds(tokenProperties.getRefreshTokenTimeOut()), Duration.ofSeconds(0));
            }
            case CLIENT_USER -> {
            }
            default -> log.warn("{} 发生刷新TOKEN事件, 忽略处理", baseUser);
        }
    }
}
