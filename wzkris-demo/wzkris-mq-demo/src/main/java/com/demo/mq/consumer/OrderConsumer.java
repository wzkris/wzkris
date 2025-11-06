package com.demo.mq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
public class OrderConsumer {

    @Bean
    public Consumer<String> order() {
        return body -> log.info("order消费: {}", body);
    }

    @Bean
    public Consumer<String> orderPay() {
        return body -> log.info("orderPay消费: {}", body);
    }

}
