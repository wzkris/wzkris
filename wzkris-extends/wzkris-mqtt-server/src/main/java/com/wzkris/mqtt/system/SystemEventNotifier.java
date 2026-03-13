package com.wzkris.mqtt.system;

import com.wzkris.mqtt.model.MqttSession;

/**
 * 系统事件发布契约。
 */
public interface SystemEventNotifier {

    void clientConnected(MqttSession session);

    void clientDisconnected(MqttSession session, String reason);

}
