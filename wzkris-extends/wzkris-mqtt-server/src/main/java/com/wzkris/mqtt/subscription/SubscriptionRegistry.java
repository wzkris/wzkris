package com.wzkris.mqtt.subscription;

import com.wzkris.mqtt.model.MqttSession;
import com.wzkris.mqtt.model.RouteTargets;
import io.vertx.mqtt.MqttTopicSubscription;

import java.util.List;

/**
 * 订阅注册与匹配契约。
 */
public interface SubscriptionRegistry {

    void addSubscriptions(MqttSession session, List<MqttTopicSubscription> subs);

    void removeSubscriptions(MqttSession session, List<String> topics);

    void removeAll(MqttSession session);

    List<MqttSession> match(String topic);

    RouteTargets matchForPublish(String topic);

}
