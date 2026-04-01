package com.wzkris.mqtt.session;

import com.wzkris.mqtt.model.MqttSession;
import com.wzkris.mqtt.model.SessionRegistration;
import io.vertx.mqtt.MqttEndpoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理当前节点上的 MQTT 会话。
 */
public class DefaultSessionRegistry implements SessionRegistry {

    private final Map<String, MqttSession> sessionsByClientId = new ConcurrentHashMap<>();

    @Override
    public SessionRegistration register(String nodeId, MqttEndpoint endpoint) {
        MqttSession newSession = new MqttSession(nodeId, endpoint);
        MqttSession previousSession = sessionsByClientId.put(newSession.getClientId(), newSession);
        return new SessionRegistration(newSession, previousSession);
    }

    @Override
    public void unregister(MqttSession session) {
        if (session == null) {
            return;
        }
        sessionsByClientId.remove(session.getClientId(), session);
    }

    @Override
    public MqttSession getByClientId(String clientId) {
        return sessionsByClientId.get(clientId);
    }

    @Override
    public Collection<MqttSession> allSessions() {
        return Collections.unmodifiableList(new ArrayList<>(sessionsByClientId.values()));
    }

}
