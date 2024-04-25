package com.thingslink.order.mqconsumer;

import com.thingslink.common.security.model.LoginSysUser;
import com.thingslink.common.core.utils.json.JsonUtil;
import com.thingslink.common.mq.constant.MQTopic;
import com.thingslink.order.service.ChargingOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 消费者demo
 * @date : 2023/5/2 13:31
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQConsumer {

    private final ChargingOrderService orderService;

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = MQTopic.TEST)
    public LoginSysUser TEST(@Payload LoginSysUser loginUser) {
        System.out.println(loginUser);
        return new LoginSysUser();
    }

    @RabbitListener(queues = MQTopic.ORDER_CLOSE)
    public void ORDER_CLOSE(Message message) {
        System.out.println(JsonUtil.parseObject(message.getBody(), LoginSysUser.class));
    }
}
