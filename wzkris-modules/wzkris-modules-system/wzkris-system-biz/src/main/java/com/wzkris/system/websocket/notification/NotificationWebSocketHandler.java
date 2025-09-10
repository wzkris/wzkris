package com.wzkris.system.websocket.notification;

import com.wzkris.system.constant.WsProtocolConstants;
import com.wzkris.system.utils.WebSocketSessionHolder;
import com.wzkris.system.websocket.BaseWebSocketHandler;
import com.wzkris.system.websocket.handler.AuthHandler;
import com.wzkris.system.websocket.handler.HeartBeatHandler;
import com.wzkris.system.websocket.protocol.WsMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.nio.ByteBuffer;

@Slf4j
@Component
public class NotificationWebSocketHandler extends BaseWebSocketHandler {

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        ByteBuffer buffer = message.getPayload();

        try {
            // 解析消息头
            WsMessage wsMessage = WsMessage.fromByteBuffer(buffer);

            // 根据消息类型进行处理
            switch (wsMessage.getType()) {
                case WsProtocolConstants.TYPE_HEARTBEAT:
                    HeartBeatHandler.handle(session, wsMessage, this::closeSession);
                    break;
                case WsProtocolConstants.TYPE_AUTH:
                    AuthHandler.handle(session, wsMessage, this::closeSession);
                    break;
                case WsProtocolConstants.TYPE_CHAT:
                    // do chat
                    break;
                default:
                    closeSession(session, CloseStatus.NOT_ACCEPTABLE);
                    break;
            }
        } catch (IllegalArgumentException e) {
            log.warn("消息格式错误: {}", e.getMessage());
            closeSession(session, CloseStatus.BAD_DATA);
        } catch (Exception e) {
            log.error("处理二进制消息异常", e);
            closeSession(session, CloseStatus.SERVER_ERROR);
        }
    }

    /**
     * 安全关闭会话
     */
    private void closeSession(WebSocketSession session, CloseStatus status) {
        try {
            if (session.isOpen()) {
                WebSocketSessionHolder.removeSession(getLoginInfo(session).getId());
                session.close(status);
            }
        } catch (IOException e) {
            log.error("关闭连接发生异常", e);
        }
    }

}
