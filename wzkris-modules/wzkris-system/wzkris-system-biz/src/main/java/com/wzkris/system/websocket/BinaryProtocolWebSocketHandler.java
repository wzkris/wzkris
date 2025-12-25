package com.wzkris.system.websocket;

import com.wzkris.system.utils.WebSocketSessionHolder;
import com.wzkris.system.websocket.handler.AuthHandler;
import com.wzkris.system.websocket.handler.ChatHandler;
import com.wzkris.system.websocket.handler.HeartBeatHandler;
import com.wzkris.system.websocket.protocol.WsMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 自定义二进制协议
 * <p>
 * 心跳包  下行  无消息体
 * 0x01  0x01  0x0000
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryProtocolWebSocketHandler extends BaseWebSocketHandler {

    private final ChatHandler chatHandler;

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        ByteBuffer buffer = message.getPayload();

        try {
            // 解析消息头
            WsMessage wsMessage = WsMessage.fromByteBuffer(buffer);

            if (wsMessage.isDownDirection()) {// 接收到的必须是上行数据
                closeSession(session, CloseStatus.PROTOCOL_ERROR);
            }

            if (wsMessage.isHeartbeat()) {
                HeartBeatHandler.handle(session, wsMessage, this::closeSession);
            } else if (wsMessage.isAuth()) {
                AuthHandler.handle(session, wsMessage, this::closeSession);
            } else if (wsMessage.isChat()) {
                chatHandler.handle(session, wsMessage, this::closeSession);
            } else {
                closeSession(session, CloseStatus.NOT_ACCEPTABLE);
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
