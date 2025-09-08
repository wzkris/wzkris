package com.wzkris.system.listener;

import com.wzkris.system.listener.event.NotificationEvent;
import com.wzkris.system.utils.GlobalSseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 系统消息推送
 * @since : 2024/12/28 16:50
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PublishMessageListener {

    @Async
    @EventListener
    public void pushEvent(NotificationEvent messageEvent) {
        GlobalSseUtil.publish(messageEvent);
    }

}
