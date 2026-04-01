package com.wzkris.mqtt.router;

import com.wzkris.mqtt.model.MqttSession;
import com.wzkris.mqtt.model.RouteResult;
import com.wzkris.mqtt.model.RouteTargets;
import com.wzkris.mqtt.subscription.SubscriptionRegistry;
import io.vertx.mqtt.messages.MqttPublishMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 消息路由器：根据 topic / QoS 等决定如何处理客户端 PUBLISH 的消息。
 * <p>
 * 当前职责：按订阅关系把消息投递到目标会话（普通订阅全量 + 共享订阅单一会话）。
 * </p>
 */
public class DefaultMessageRouter implements MessageRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMessageRouter.class);

    private final SubscriptionRegistry subscriptionRegistry;

    public DefaultMessageRouter(SubscriptionRegistry subscriptionRegistry) {
        this.subscriptionRegistry = subscriptionRegistry;
    }

    @Override
    public RouteResult route(MqttSession publisherSession, MqttPublishMessage message) {
        String topic = message.topicName();
        RouteTargets routeTargets = subscriptionRegistry.matchForPublish(topic);
        Set<MqttSession> mergedTargets = mergeTargets(routeTargets);

        int deliveredCount = 0;
        for (MqttSession target : mergedTargets) {
            if (!shouldDeliverToSession(publisherSession, target, topic)) {
                continue;
            }
            target.getEndpoint().publish(
                    topic,
                    message.payload(),
                    message.qosLevel(),
                    message.isDup(),
                    message.isRetain());
            deliveredCount++;
        }

        LOGGER.info(
                "mqtt.route clientId={} topic={} qos={} payloadSize={} normalCount={} sharedTarget={} deliveredCount={}",
                publisherSession.getClientId(),
                topic,
                message.qosLevel(),
                message.payload() != null ? message.payload().length() : 0,
                routeTargets.getNormalTargets().size(),
                routeTargets.getSharedTarget() != null ? routeTargets.getSharedTarget().getClientId() : "none",
                deliveredCount);
        return new RouteResult(
                routeTargets.getNormalTargets().size(),
                routeTargets.getSharedTarget() != null,
                deliveredCount);
    }

    /**
     * Rule: publisher can receive its own message when it also subscribes the topic.
     */
    private boolean shouldDeliverToSession(MqttSession publisherSession, MqttSession targetSession, String topic) {
        return targetSession != null;
    }

    private Set<MqttSession> mergeTargets(RouteTargets routeTargets) {
        Set<MqttSession> mergedTargets = new LinkedHashSet<>(routeTargets.getNormalTargets());
        if (routeTargets.getSharedTarget() != null) {
            mergedTargets.add(routeTargets.getSharedTarget());
        }
        return mergedTargets;
    }

}
