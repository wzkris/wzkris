package com.demo.mq.producer;

import com.alibaba.cloud.stream.binder.rocketmq.constant.RocketMQConst;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {

    private final StreamBridge streamBridge;

    public OrderProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public boolean send(String messageKey, String payload) {
        return send(messageKey, null, payload);
    }

    public boolean send(String messageKey, String tag, String payload) {
        return streamBridge.send("order-out-0", MessageBuilder.withPayload(payload)
                .setHeader(RocketMQConst.Headers.KEYS, messageKey)
                .setHeader(RocketMQConst.Headers.TAGS, tag)
                .build());
    }

}


