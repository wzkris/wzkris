package com.wzkris.mqtt.routing;

import com.wzkris.mqtt.session.MqttSession;
import io.vertx.mqtt.messages.MqttPublishMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息路由器：根据 topic / QoS 等决定如何处理客户端 PUBLISH 的消息。
 * <p>
 * 目前仅记录日志与基本 ACK，后续可扩展为路由到业务处理器、消息队列等。
 * </p>
 */
public class MessageRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRouter.class);

    public void route(MqttSession session, MqttPublishMessage message) {
        // 目前只做简单日志，保留扩展点
        LOGGER.info(
                "Route message from client [{}] topic [{}] qos [{}] payloadSize={}",
                session.getClientId(),
                message.topicName(),
                message.qosLevel(),
                message.payload() != null ? message.payload().length() : 0);
    }

}

