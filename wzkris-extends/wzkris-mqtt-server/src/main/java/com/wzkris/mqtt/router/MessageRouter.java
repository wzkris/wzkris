package com.wzkris.mqtt.router;

import com.wzkris.mqtt.model.MqttSession;
import com.wzkris.mqtt.model.RouteResult;
import io.vertx.mqtt.messages.MqttPublishMessage;

/**
 * 客户端发布消息的路由契约。
 */
public interface MessageRouter {

    RouteResult route(MqttSession publisherSession, MqttPublishMessage message);

}
