package com.wzkris.common.mq.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rabbitMQ配置
 * @date : 2023/12/7 12:38
 */
public class StreamConfig {

    /**
     * 使用jackson序列化
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
