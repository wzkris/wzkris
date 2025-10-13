package com.wzkris.message.websocket.handler;

import com.wzkris.message.websocket.protocol.WsMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.util.function.BiConsumer;

/**
 * 认证处理
 */
@Slf4j
public class AuthHandler {

    /**
     * 处理心跳消息
     */
    public static void handle(WebSocketSession session, WsMessage message, BiConsumer<WebSocketSession, CloseStatus> closeSession) {
        if (!message.hasBody()) {
            closeSession.accept(session, CloseStatus.BAD_DATA);
        }

        // do auth
    }

}
