package com.wzkris.mqtt.server;

import com.wzkris.mqtt.handler.MqttEndpointHandler;
import com.wzkris.mqtt.routing.MessageRouter;
import com.wzkris.mqtt.session.MqttSession;
import com.wzkris.mqtt.session.MqttSessionManager;
import com.wzkris.mqtt.subscription.SubscriptionManager;
import com.wzkris.mqtt.subscription.TopicMatcher;
import com.wzkris.mqtt.system.SystemEventPublisher;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.mqtt.MqttEndpoint;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MqttServerVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttServerVerticle.class);

    private static final int DEFAULT_PORT = 1883;

    private static final String DEFAULT_HOST = "0.0.0.0";

    /**
     * 当前节点标识：ip:port
     */
    private String nodeId;

    private MqttSessionManager sessionManager;

    private SubscriptionManager subscriptionManager;

    private SystemEventPublisher systemEventPublisher;

    private MessageRouter messageRouter;

    private MqttEndpointHandler endpointHandler;

    @Override
    public void start(Promise<Void> startPromise) {
        this.sessionManager = new MqttSessionManager();
        this.subscriptionManager = new SubscriptionManager(new TopicMatcher());
        this.systemEventPublisher = new SystemEventPublisher(subscriptionManager);
        this.messageRouter = new MessageRouter();
        this.endpointHandler = new MqttEndpointHandler(
                sessionManager, subscriptionManager, systemEventPublisher, messageRouter);

        MqttServerOptions options = new MqttServerOptions()
                .setPort(DEFAULT_PORT)
                .setHost(DEFAULT_HOST);

        MqttServer mqttServer = MqttServer.create(vertx, options);

        mqttServer
                .endpointHandler(this::handleEndpoint)
                .listen()
                .onSuccess(server -> {
                    int port = server.actualPort();
                    String ip = resolveLocalIp();
                    this.nodeId = ip + ":" + port;
                    LOGGER.info("MQTT server started on {} (nodeId={})", this.nodeId, this.nodeId);
                    startPromise.complete();
                })
                .onFailure(throwable -> {
                    LOGGER.error("Failed to start MQTT server", throwable);
                    startPromise.fail(throwable);
                });
    }

    private String resolveLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            LOGGER.warn("Failed to resolve local host address, fallback to 127.0.0.1", ex);
            return "127.0.0.1";
        }
    }

    private void handleEndpoint(MqttEndpoint endpoint) {
        LOGGER.info(
                "MQTT client [{}] request to connect, clean session = {}",
                endpoint.clientIdentifier(), endpoint.isCleanSession());

        if (endpoint.will() != null) {
            LOGGER.info(
                    "Will message: topic = {}, QoS = {}, isRetain = {}",
                    endpoint.will().getWillTopic(),
                    endpoint.will().getWillQos(),
                    endpoint.will().isWillRetain());
        }

        endpoint.accept(endpoint.isCleanSession());

        MqttSession session = sessionManager.register(nodeId, endpoint);
        endpointHandler.attachTo(session);
    }

}
