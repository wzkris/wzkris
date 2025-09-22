package com.wzkris.system.websocket;

import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.system.utils.WebSocketSessionHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

@Slf4j
public abstract class BaseWebSocketHandler extends BinaryWebSocketHandler {

    public static CorePrincipal getLoginInfo(WebSocketSession session) {
        return (CorePrincipal) ((UsernamePasswordAuthenticationToken) session.getPrincipal()).getPrincipal();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        CorePrincipal principal = getLoginInfo(session);

        WebSocketSession previous = WebSocketSessionHolder.addSession(principal.getId(), session);
        if (previous != null) {
            // 不为空则需要关闭当前连接
            session.close(CloseStatus.POLICY_VIOLATION);
        }

        log.info("WebSocket连接建立 - 用户: {}, 会话ID: {}, 当前连接数: {}",
                principal.getId(), session.getId(), WebSocketSessionHolder.getSessionCount());
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        CorePrincipal principal = getLoginInfo(session);

        log.info("WebSocket处理pong心跳, 用户: {}, 会话ID: {}, 当前连接数: {}",
                principal.getId(), session.getId(), WebSocketSessionHolder.getSessionCount());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        CorePrincipal loginInfo = getLoginInfo(session);

        WebSocketSessionHolder.removeSession(loginInfo.getId());
        log.info("WebSocket连接关闭 - 状态: {}, 当前连接数: {}", status, WebSocketSessionHolder.getSessionCount());
    }

}
