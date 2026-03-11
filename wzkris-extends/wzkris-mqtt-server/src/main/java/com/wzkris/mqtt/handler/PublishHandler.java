package com.wzkris.mqtt.handler;

import com.wzkris.mqtt.routing.MessageRouter;
import com.wzkris.mqtt.session.MqttSession;
import io.vertx.mqtt.messages.MqttPublishMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PUBLISH / PUBREL / ACK 相关事件处理。
 */
public class PublishHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublishHandler.class);

    private final MessageRouter messageRouter;

    public PublishHandler(MessageRouter messageRouter) {
        this.messageRouter = messageRouter;
    }

    public void onPublish(MqttSession session, MqttPublishMessage message) {
        String topic = message.topicName();
        LOGGER.info(
                "Received message on topic [{}] from client [{}], QoS = {}, payload size = {}",
                topic,
                session.getClientId(),
                message.qosLevel(),
                message.payload() != null ? message.payload().length() : 0);

        // 业务路由
        messageRouter.route(session, message);

        // ACK 行为
        int qos = message.qosLevel().value();
        if (qos == 1) {
            session.getEndpoint().publishAcknowledge(message.messageId());
        } else if (qos == 2) {
            session.getEndpoint().publishReceived(message.messageId());
        }
    }

    public void onPublishRelease(MqttSession session, int messageId) {
        session.getEndpoint().publishComplete(messageId);
    }

}

