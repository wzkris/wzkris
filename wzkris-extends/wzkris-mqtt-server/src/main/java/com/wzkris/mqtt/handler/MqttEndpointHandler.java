package com.wzkris.mqtt.handler;

import com.wzkris.mqtt.routing.MessageRouter;
import com.wzkris.mqtt.session.MqttSession;
import com.wzkris.mqtt.session.MqttSessionManager;
import com.wzkris.mqtt.subscription.SubscriptionManager;
import com.wzkris.mqtt.system.SystemEventPublisher;
import io.vertx.mqtt.MqttEndpoint;

/**
 * 负责把各个业务 handler 安装到 {@link MqttEndpoint} 上的装配类本身不包含具体业务逻辑。
 */
public class MqttEndpointHandler {

    private final ConnectionHandler connectionHandler;

    private final SubscriptionHandler subscriptionHandler;

    private final PublishHandler publishHandler;

    public MqttEndpointHandler(MqttSessionManager sessionManager,
                               SubscriptionManager subscriptionManager,
                               SystemEventPublisher systemEventPublisher,
                               MessageRouter messageRouter) {
        this.connectionHandler = new ConnectionHandler(sessionManager, subscriptionManager, systemEventPublisher);
        this.subscriptionHandler = new SubscriptionHandler(subscriptionManager);
        this.publishHandler = new PublishHandler(messageRouter);
    }

    /**
     * 将连接、断联、订阅、发布等 handler 挂载到 endpoint。
     *
     * @param session  已注册的会话
     */
    public void attachTo(MqttSession session) {
        connectionHandler.onConnected(session);

        MqttEndpoint endpoint = session.getEndpoint();

        endpoint.disconnectMessageHandler(msg -> connectionHandler.onDisconnectMessage(session, msg));
        endpoint.disconnectHandler(v -> connectionHandler.onDisconnected(session));
        endpoint.closeHandler(v -> connectionHandler.onClose(session));

        endpoint.subscribeHandler(subscribe ->
                subscriptionHandler.onSubscribe(session, subscribe));

        endpoint.unsubscribeHandler(unsubscribe ->
                subscriptionHandler.onUnsubscribe(session, unsubscribe));

        endpoint.publishHandler(message ->
                publishHandler.onPublish(session, message));

        endpoint.publishReleaseHandler(messageId ->
                publishHandler.onPublishRelease(session, messageId));
    }

}
