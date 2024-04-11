package com.thingslink.common.mq.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author wz
 * @version 1.0
 * @description mq服务
 * @date 2022/11/8 13:49
 */
public class MQService {
    private static final Logger log = LoggerFactory.getLogger(MQService.class);
    private final RabbitTemplate rabbitTemplate;

    public MQService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(String exchange, String routingKey, Object message) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
        } catch (Exception e) {
            log.error("发送消息失败", e);
        }
    }

}
