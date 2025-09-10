package com.wzkris.system.websocket.handler;

import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.system.constant.WsProtocolConstants;
import com.wzkris.system.utils.WebSocketSessionHolder;
import com.wzkris.system.websocket.protocol.WsMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * 心跳处理
 */
@Slf4j
public class HeartBeatHandler {

    /**
     * 处理心跳消息
     */
    public static void handle(WebSocketSession session, WsMessage message, BiConsumer<WebSocketSession, CloseStatus> closeSession) {
        if (message.getDirection() == WsProtocolConstants.DIRECTION_UP) {
            // 客户端发送的心跳
            log.info("收到客户端心跳，数据长度: {}", message.getLength());

            // 处理多连接检测
            CorePrincipal principal = (CorePrincipal) session.getAttributes().get(CorePrincipal.class.getName());
            WebSocketSession current = WebSocketSessionHolder.getSession(principal.getId());
            if (current != null && !current.getId().equals(session.getId())) {
                log.warn("用户 {} 存在多个连接，关闭新连接 {}", principal.getId(), session.getId());
                closeSession.accept(session, CloseStatus.SESSION_NOT_RELIABLE);
                return;
            }

            // 更新会话持有器
            WebSocketSessionHolder.addSession(principal.getId(), session);

            // 发送心跳响应（下行）
            sendHeartbeatResponse(session);
        } else {
            // 服务器发送的心跳（通常不会收到）
            log.debug("收到服务器心跳响应");
        }
    }

    /**
     * 发送心跳响应
     */
    private static void sendHeartbeatResponse(WebSocketSession session) {
        try {
            WsMessage response = WsMessage.heartBeat(WsProtocolConstants.DIRECTION_DOWN);
            session.sendMessage(WsMessage.convertToBinaryMessage(response));
        } catch (IOException e) {
            log.error("发送心跳响应失败", e);
        }
    }

}
