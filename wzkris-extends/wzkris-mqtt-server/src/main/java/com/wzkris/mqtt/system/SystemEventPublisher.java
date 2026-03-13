package com.wzkris.mqtt.system;

import com.wzkris.mqtt.model.MqttSession;
import com.wzkris.mqtt.subscription.SubscriptionRegistry;
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
public class SystemEventPublisher implements SystemEventNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemEventPublisher.class);

    private final SubscriptionRegistry subscriptionRegistry;

    public SystemEventPublisher(SubscriptionRegistry subscriptionRegistry) {
        this.subscriptionRegistry = subscriptionRegistry;
    }

    @Override
    public void clientConnected(MqttSession session) {
        RemoteEndpoint remote = resolveRemote(session);
        long now = System.currentTimeMillis();

        String payloadJson = '{'
                + "\"node\":\"" + session.getNodeId() + "\","
                + "\"clientid\":\"" + session.getClientId() + "\","
                + "\"username\":\"" + session.getUsername() + "\","
                + "\"clean_session\":" + session.isCleanSession() + ','
                + "\"keepalive\":" + session.getKeepAliveTimeSeconds() + ','
                + "\"ipaddress\":\"" + remote.host + "\","
                + "\"sockport\":" + remote.port + ','
                + "\"ts\":" + now
                + '}';

        String topic = MqttSystemTopics.CONNECTED;
        LOGGER.debug("Publish client connected event to topic {} payload {}", topic, payloadJson);
        broadcast(topic, payloadJson);
    }

    @Override
    public void clientDisconnected(MqttSession session, String reason) {
        RemoteEndpoint remote = resolveRemote(session);
        long now = System.currentTimeMillis();

        String payloadJson = '{'
                + "\"node\":\"" + session.getNodeId() + "\","
                + "\"username\":\"" + session.getUsername() + "\","
                + "\"ts\":" + now + ','
                + "\"sockport\":" + remote.port + ','
                + "\"reason\":\"" + reason + "\","
                + "\"ipaddress\":\"" + remote.host + "\","
                + "\"disconnected_at\":" + now + ','
                + "\"clientid\":\"" + session.getClientId() + "\""
                + '}';

        String topic = MqttSystemTopics.DISCONNECTED;
        LOGGER.debug("Publish client disconnected event to topic {} payload {}", topic, payloadJson);
        broadcast(topic, payloadJson);
    }

    private void broadcast(String topic, String json) {
        Buffer payload = Buffer.buffer(json);
        List<MqttSession> targets = subscriptionRegistry.match(topic);
        for (MqttSession target : targets) {
            target.getEndpoint()
                    .publish(topic, payload, MqttQoS.AT_LEAST_ONCE, false, false);
        }
    }

    private RemoteEndpoint resolveRemote(MqttSession session) {
        SocketAddress remote = session.getRemoteAddress();
        if (remote == null) {
            return new RemoteEndpoint("0.0.0.0", -1);
        }
        return new RemoteEndpoint(remote.host(), remote.port());
    }

    private static final class RemoteEndpoint {

        private final String host;

        private final int port;

        private RemoteEndpoint(String host, int port) {
            this.host = host;
            this.port = port;
        }

    }

}
