package com.wzkris.mqtt.session;

import com.wzkris.mqtt.model.MqttSession;
import com.wzkris.mqtt.model.SessionRegistration;
import io.vertx.mqtt.MqttEndpoint;

import java.util.Collection;

/**
 * 节点内 MQTT 会话注册表契约。
 */
public interface SessionRegistry {

    SessionRegistration register(String nodeId, MqttEndpoint endpoint);

    void unregister(MqttSession session);

    MqttSession getByClientId(String clientId);

    Collection<MqttSession> allSessions();

}
