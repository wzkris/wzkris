package com.wzkris.equipment.stream;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class MyProducer {

    @Bean("Aproducer")
    public Supplier<Message<String>> producer() {
        return () -> {
            return MessageBuilder
                    .withPayload("123")
                    .build();
        };
    }
}
