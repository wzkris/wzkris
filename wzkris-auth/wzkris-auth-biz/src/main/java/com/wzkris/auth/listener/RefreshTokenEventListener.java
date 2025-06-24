package com.wzkris.auth.listener;

import com.wzkris.auth.listener.event.RefreshTokenEvent;
import com.wzkris.auth.rmi.enums.AuthenticatedType;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.core.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 刷新Token事件监听
 * @date : 2023/8/28 10:05
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenEventListener {

    private final TokenService tokenService;

    @Async
    @EventListener
    public void refreshTokenEvent(RefreshTokenEvent event) {
        CorePrincipal principal = event.getPrincipal();

        if (StringUtil.equals(principal.getType(), AuthenticatedType.SYSTEM_USER.getValue())) {

        } else if (StringUtil.equals(principal.getType(), AuthenticatedType.CLIENT_USER.getValue())) {
            // empty
        } else {
            log.warn("{} 发生刷新TOKEN事件, 忽略处理", principal);
        }
    }

}
