package com.wzkris.auth.listener;

import com.wzkris.auth.listener.event.LogoutEvent;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.enums.AuthType;
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
        log.info("id '{}'的{}用户退出登录", event.getId(), event.getUserType());

        if (StringUtil.equals(event.getUserType(), AuthType.USER.getValue())) {

        } else if (StringUtil.equals(event.getUserType(), AuthType.CUSTOMER.getValue())) {

        }
    }

}
