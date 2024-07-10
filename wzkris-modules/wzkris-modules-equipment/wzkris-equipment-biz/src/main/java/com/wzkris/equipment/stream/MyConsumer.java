package com.wzkris.equipment.stream;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class MyConsumer {

    @Bean
    public Consumer<Message<String>> consumer() {
        return (data) -> {
            String payload = data.getPayload();
            System.out.println("consumer 接收一条记录：" + payload);
        };
    }
}
