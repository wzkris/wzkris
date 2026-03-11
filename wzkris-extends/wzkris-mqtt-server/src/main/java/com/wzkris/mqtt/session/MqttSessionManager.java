package com.wzkris.mqtt.session;

import io.vertx.mqtt.MqttEndpoint;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理当前节点上的 MQTT 会话。
 */
public class MqttSessionManager {

    private final Map<String, MqttSession> sessionsByClientId = new ConcurrentHashMap<>();

    private final Set<MqttSession> sessions = ConcurrentHashMap.newKeySet();

    public MqttSession register(String nodeId, MqttEndpoint endpoint) {
        MqttSession session = new MqttSession(nodeId, endpoint);
        sessions.add(session);
        sessionsByClientId.put(session.getClientId(), session);
        return session;
    }

    public void unregister(MqttSession session) {
        if (session == null) {
            return;
        }
        sessions.remove(session);
        sessionsByClientId.remove(session.getClientId(), session);
    }

    public MqttSession getByClientId(String clientId) {
        return sessionsByClientId.get(clientId);
    }

    public Collection<MqttSession> allSessions() {
        return sessions;
    }

}

