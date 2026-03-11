package com.wzkris.mqtt.session;

import io.vertx.core.net.SocketAddress;
import io.vertx.mqtt.MqttEndpoint;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 对 MQTT 客户端连接的封装，会话级信息。
 */
public class MqttSession {

    private final String nodeId;

    private final MqttEndpoint endpoint;

    /**
     * 断联事件是否已发送（三种断联回调中只发一次）
     */
    private final AtomicBoolean disconnectEventSent = new AtomicBoolean(false);

    public MqttSession(String nodeId, MqttEndpoint endpoint) {
        this.nodeId = nodeId;
        this.endpoint = endpoint;
    }

    public String getNodeId() {
        return nodeId;
    }

    public MqttEndpoint getEndpoint() {
        return endpoint;
    }

    public String getClientId() {
        return endpoint.clientIdentifier();
    }

    public String getUsername() {
        return endpoint.auth() != null ? endpoint.auth().getUsername() : null;
    }

    public boolean isCleanSession() {
        return endpoint.isCleanSession();
    }

    public int getKeepAliveTimeSeconds() {
        return endpoint.keepAliveTimeSeconds();
    }

    public SocketAddress getRemoteAddress() {
        return endpoint.remoteAddress();
    }

    /**
     * 标记断联事件已发送。仅第一次调用返回 true，用于三种断联回调中“只发一次”的语义。
     *
     * @return true 表示由本次调用完成标记（应发送断联消息），false 表示已由其他回调发送过
     */
    public boolean tryMarkDisconnectEventSent() {
        return disconnectEventSent.compareAndSet(false, true);
    }

}

