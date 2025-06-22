package com.wzkris.auth.listener;

import com.wzkris.auth.domain.OnlineUser;
import com.wzkris.auth.listener.event.RefreshTokenEvent;
import com.wzkris.auth.rmi.domain.SystemUser;
import com.wzkris.auth.rmi.enums.AuthenticatedType;
import com.wzkris.auth.security.config.TokenProperties;
import com.wzkris.auth.utils.OnlineUserUtil;
import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.core.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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

    @Async
    @EventListener
    public void refreshTokenEvent(RefreshTokenEvent event) {
        CorePrincipal principal = event.getPrincipal();

        if (StringUtil.equals(principal.getType(), AuthenticatedType.SYSTEM_USER.getValue())) {
            SystemUser user = (SystemUser) principal;
            RMapCache<String, OnlineUser> onlineCache = OnlineUserUtil.getOnlineCache(user.getUserId());
            onlineCache.expireEntry(
                    principal.getId(),
                    Duration.ofSeconds(tokenProperties.getRefreshTokenTimeOut()),
                    Duration.ofSeconds(0));
        } else if (StringUtil.equals(principal.getType(), AuthenticatedType.CLIENT_USER.getValue())) {
            // empty
        } else {
            log.warn("{} 发生刷新TOKEN事件, 忽略处理", principal);
        }
    }

}
