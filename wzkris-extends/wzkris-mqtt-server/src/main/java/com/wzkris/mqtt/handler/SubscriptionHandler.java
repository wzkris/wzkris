package com.wzkris.mqtt.handler;

import com.wzkris.mqtt.model.MqttSession;
import com.wzkris.mqtt.subscription.SubscriptionRegistry;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.mqtt.MqttTopicSubscription;
import io.vertx.mqtt.messages.MqttSubscribeMessage;
import io.vertx.mqtt.messages.MqttUnsubscribeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订阅 / 退订相关事件处理。
 */
public class SubscriptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionHandler.class);

    private final SubscriptionRegistry subscriptionRegistry;

    public SubscriptionHandler(SubscriptionRegistry subscriptionRegistry) {
        this.subscriptionRegistry = subscriptionRegistry;
    }

    public void onSubscribe(MqttSession session, MqttSubscribeMessage subscribe) {
        List<MqttTopicSubscription> subscriptions = subscribe.topicSubscriptions();

        String topics = subscriptions.stream()
                .map(s -> s.topicName() + " (" + s.qualityOfService() + ")")
                .collect(Collectors.joining(", "));

        LOGGER.info("Client [{}] subscribe topics: {}", session.getClientId(), topics);

        subscriptionRegistry.addSubscriptions(session, subscriptions);

        List<MqttQoS> grantedQosLevels = subscriptions.stream()
                .map(MqttTopicSubscription::qualityOfService)
                .collect(Collectors.toList());

        session.getEndpoint().subscribeAcknowledge(subscribe.messageId(), grantedQosLevels);
    }

    public void onUnsubscribe(MqttSession session,
                              MqttUnsubscribeMessage unsubscribe) {
        LOGGER.info("Client [{}] unsubscribe topics {}",
                session.getClientId(), unsubscribe.topics());
        subscriptionRegistry.removeSubscriptions(session, unsubscribe.topics());
    }

}
