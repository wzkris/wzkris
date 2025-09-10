package com.wzkris.system.listener;

import com.wzkris.system.listener.event.PubNotificationEvent;
import com.wzkris.system.utils.WebSocketSessionHolder;
import com.wzkris.system.websocket.protocol.WsMessage;
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
public class PubNotificationListener {

    @Async
    @EventListener
    public void pubNotificationEvent(PubNotificationEvent event) {
        event.getIds().forEach(id -> {
            WebSocketSessionHolder.sendMessage(id, WsMessage.convertToBinaryMessage(WsMessage.notification(event.getMessage())));
        });
    }

}
