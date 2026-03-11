package com.wzkris.mqtt.handler;

import com.wzkris.mqtt.session.MqttSession;
import com.wzkris.mqtt.subscription.SubscriptionManager;
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

    private final SubscriptionManager subscriptionManager;

    public SubscriptionHandler(SubscriptionManager subscriptionManager) {
        this.subscriptionManager = subscriptionManager;
    }

    public void onSubscribe(MqttSession session, MqttSubscribeMessage subscribe) {
        List<MqttTopicSubscription> subscriptions = subscribe.topicSubscriptions();

        String topics = subscriptions.stream()
                .map(s -> s.topicName() + " (" + s.qualityOfService() + ")")
                .collect(Collectors.joining(", "));

        LOGGER.info("Client [{}] subscribe topics: {}", session.getClientId(), topics);

        subscriptionManager.addSubscriptions(session, subscriptions);

        List<MqttQoS> grantedQosLevels = subscriptions.stream()
                .map(MqttTopicSubscription::qualityOfService)
                .collect(Collectors.toList());

        session.getEndpoint().subscribeAcknowledge(subscribe.messageId(), grantedQosLevels);
    }

    public void onUnsubscribe(MqttSession session,
                              MqttUnsubscribeMessage unsubscribe) {
        LOGGER.info("Client [{}] unsubscribe topics {}",
                session.getClientId(), unsubscribe.topics());
        subscriptionManager.removeSubscriptions(session, unsubscribe.topics());
    }

}

