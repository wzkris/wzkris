package com.wzkris.auth.listener;

import com.wzkris.auth.event.LogoutEvent;
import com.wzkris.common.core.enums.AuthTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogoutEventListener {

    @Async
    @EventListener
    public void logoutEvent(LogoutEvent event) {
        log.info("id '{}'的{}用户退出登录", event.getUid(), event.getAuthType());

        AuthTypeEnum authType = event.getAuthType();
        if (authType == AuthTypeEnum.ADMIN) {

        } else if (authType == AuthTypeEnum.TENANT) {

        } else if (authType == AuthTypeEnum.CUSTOMER) {

        }
    }

}
