package com.wzkris.mqtt;

import com.wzkris.mqtt.server.MqttServerVerticle;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MqttServerApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttServerApplication.class);

    private MqttServerApplication() {
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MqttServerVerticle())
                .onFailure(throwable -> {
                    LOGGER.error("Failed to deploy MQTT server verticle", throwable);
                    vertx.close();
                });
    }

}
