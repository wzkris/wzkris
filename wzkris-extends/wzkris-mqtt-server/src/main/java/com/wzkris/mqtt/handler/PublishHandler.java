package com.wzkris.mqtt.handler;

import com.wzkris.mqtt.model.MqttSession;
import com.wzkris.mqtt.model.RouteResult;
import com.wzkris.mqtt.router.MessageRouter;
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
        RouteResult routeResult = messageRouter.route(session, message);
        LOGGER.info(
                "mqtt.publish clientId={} topic={} qos={} payloadSize={} normalCount={} sharedMatched={} deliveredCount={}",
                session.getClientId(),
                topic,
                message.qosLevel(),
                message.payload() != null ? message.payload().length() : 0,
                routeResult.getNormalTargetCount(),
                routeResult.isSharedTargetMatched(),
                routeResult.getDeliveredCount());

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
