package com.wzkris.auth.listener;

import com.wzkris.auth.listener.event.RefreshTokenEvent;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.model.MyPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

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
        MyPrincipal principal = event.getPrincipal();
        log.info("'{}' 发生刷新TOKEN事件", principal);

        if (Objects.equals(principal.getType(), AuthType.USER)) {

        } else if (Objects.equals(principal.getType(), AuthType.STAFF)) {

        } else if (Objects.equals(principal.getType(), AuthType.CUSTOMER)) {
            // empty
        }
    }

}
