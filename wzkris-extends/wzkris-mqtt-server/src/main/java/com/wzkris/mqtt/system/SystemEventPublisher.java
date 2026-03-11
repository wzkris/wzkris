package com.wzkris.mqtt.system;

import com.wzkris.mqtt.session.MqttSession;
import com.wzkris.mqtt.subscription.SubscriptionManager;
import com.wzkris.mqtt.topic.MqttSystemTopics;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.SocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 发布系统级事件（客户端连接/断开）到固定系统 topic，供订阅 {@code $sys/brokers/#} 或 {@code $sys/brokers/+} 的客户端接收。
 */
public class SystemEventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemEventPublisher.class);

    private final SubscriptionManager subscriptionManager;

    public SystemEventPublisher(SubscriptionManager subscriptionManager) {
        this.subscriptionManager = subscriptionManager;
    }

    public void clientConnected(MqttSession session) {
        SocketAddress remote = session.getRemoteAddress();
        String ip = remote != null ? remote.host() : "0.0.0.0";
        int port = remote != null ? remote.port() : -1;
        long now = System.currentTimeMillis();

        String json = '{'
                + "\"node\":\"" + session.getNodeId() + "\","
                + "\"clientid\":\"" + session.getClientId() + "\","
                + "\"username\":\"" + session.getUsername() + "\","
                + "\"clean_session\":" + session.isCleanSession() + ','
                + "\"keepalive\":" + session.getKeepAliveTimeSeconds() + ','
                + "\"ipaddress\":\"" + ip + "\","
                + "\"sockport\":" + port + ','
                + "\"ts\":" + now
                + '}';

        String topic = MqttSystemTopics.CONNECTED;
        LOGGER.debug("Publish client connected event to topic {} payload {}", topic, json);
        broadcast(topic, json);
    }

    public void clientDisconnected(MqttSession session, String reason) {
        SocketAddress remote = session.getRemoteAddress();
        String ip = remote.host();
        int port = remote.port();
        long now = System.currentTimeMillis();

        String json = '{'
                + "\"node\":\"" + session.getNodeId() + "\","
                + "\"username\":\"" + session.getUsername() + "\","
                + "\"ts\":" + now + ','
                + "\"sockport\":" + port + ','
                + "\"reason\":\"" + reason + "\","
                + "\"ipaddress\":\"" + ip + "\","
                + "\"disconnected_at\":" + now + ','
                + "\"clientid\":\"" + session.getClientId() + "\""
                + '}';

        String topic = MqttSystemTopics.DISCONNECTED;
        LOGGER.debug("Publish client disconnected event to topic {} payload {}", topic, json);
        broadcast(topic, json);
    }

    private void broadcast(String topic, String json) {
        Buffer payload = Buffer.buffer(json);
        List<MqttSession> targets = subscriptionManager.match(topic);
        for (MqttSession target : targets) {
            target.getEndpoint()
                    .publish(topic, payload, MqttQoS.AT_LEAST_ONCE, false, false);
        }
    }

}
