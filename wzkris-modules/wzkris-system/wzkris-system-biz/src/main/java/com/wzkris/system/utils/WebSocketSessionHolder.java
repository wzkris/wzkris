package com.wzkris.system.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.AbstractWebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * websocket持有工具
 */
@Slf4j
public class WebSocketSessionHolder {

    private final static Map<Serializable, WebSocketSession> sessions = new ConcurrentHashMap<>();

    private final static AtomicInteger sessionCount = new AtomicInteger(0);

    /**
     * 添加会话
     *
     * @param key     会话键，通常是用户ID或设备ID
     * @param session WebSocket会话
     * @return 如果之前存在会话则返回旧会话，否则返回null
     */
    public static WebSocketSession addSession(Serializable key, WebSocketSession session) {
        WebSocketSession previous = sessions.putIfAbsent(key, session);
        if (previous == null) {
            sessionCount.incrementAndGet();
        }
        return previous;
    }

    /**
     * 移除会话
     *
     * @param key 会话键
     * @return 被移除的会话，如果不存在则返回null
     */
    public static WebSocketSession removeSession(Serializable key) {
        WebSocketSession removed = sessions.remove(key);
        if (removed != null) {
            sessionCount.decrementAndGet();
        }
        return removed;
    }

    /**
     * 获取会话
     *
     * @param key 会话键
     * @return WebSocket会话，如果不存在则返回null
     */
    public static WebSocketSession getSession(Serializable key) {
        return sessions.get(key);
    }

    /**
     * 获取会话数量
     *
     * @return 当前活跃会话数量
     */
    public static int getSessionCount() {
        return sessionCount.get();
    }

    /**
     * 向指定会话发送消息
     *
     * @param key     会话键
     * @param message 消息内容
     * @return 是否发送成功
     */
    public static boolean sendMessage(Serializable key, AbstractWebSocketMessage<?> message) {
        WebSocketSession session = sessions.get(key);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(message);
                return true;
            } catch (IOException e) {
                log.error("发送消息发生异常", e);
                if (!session.isOpen()) {
                    removeSession(key);
                }
                return false;
            }
        }
        return false;
    }

}
