package com.wzkris.mqtt.handler;

import com.wzkris.mqtt.session.MqttSession;
import com.wzkris.mqtt.session.MqttSessionManager;
import com.wzkris.mqtt.subscription.SubscriptionManager;
import com.wzkris.mqtt.system.SystemEventPublisher;
import io.vertx.mqtt.messages.MqttDisconnectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 连接 / 断联相关事件处理。
 * 负责：
 * - 客户端连接成功的系统事件
 * - 三种断联回调（disconnectMessage / disconnect / close）
 * - 断联事件“只发一次、至少发一次”
 * - 会话清理
 */
public class ConnectionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionHandler.class);

    private final MqttSessionManager sessionManager;

    private final SubscriptionManager subscriptionManager;

    private final SystemEventPublisher systemEventPublisher;

    public ConnectionHandler(MqttSessionManager sessionManager,
                             SubscriptionManager subscriptionManager,
                             SystemEventPublisher systemEventPublisher) {
        this.sessionManager = sessionManager;
        this.subscriptionManager = subscriptionManager;
        this.systemEventPublisher = systemEventPublisher;
    }

    /**
     * 新连接建立后调用，发布“客户端已连接”的系统事件。
     */
    public void onConnected(MqttSession session) {
        systemEventPublisher.clientConnected(session);
        LOGGER.info("Client [{}] connected on node [{}]", session.getClientId(), session.getNodeId());
    }

    /**
     * 收到 MQTT DISCONNECT 报文。
     */
    public void onDisconnectMessage(MqttSession session,
                                    MqttDisconnectMessage message) {
        LOGGER.info("Client [{}] sent DISCONNECT (code={})",
                session.getClientId(), message.code());
        tryPublishDisconnectOnce(session, "disconnect");
    }

    /**
     * Vert.x 抛出的 MQTT 连接层断开回调。
     */
    public void onDisconnected(MqttSession session) {
        LOGGER.info("Client [{}] disconnected", session.getClientId());
        tryPublishDisconnectOnce(session, "disconnect");
    }

    /**
     * 底层 TCP 连接关闭。
     */
    public void onClose(MqttSession session) {
        LOGGER.info("Client [{}] connection closed", session.getClientId());
        tryPublishDisconnectOnce(session, "tcp_closed");
        cleanupSession(session);
    }

    private void tryPublishDisconnectOnce(MqttSession session, String reason) {
        if (session.tryMarkDisconnectEventSent()) {
            systemEventPublisher.clientDisconnected(session, reason);
        }
    }

    private void cleanupSession(MqttSession session) {
        subscriptionManager.removeAll(session);
        sessionManager.unregister(session);
    }

}

