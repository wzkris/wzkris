package com.thingslink.order;

import com.thingslink.common.core.utils.json.JsonUtil;
import com.thingslink.common.security.oauth2.model.LoginSysUser;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rabbitMQ测试
 * @date : 2023/12/7 11:08
 */
@SpringBootTest
public class RabbitTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void test() throws Exception {
        Message message = rabbitTemplate.sendAndReceive(
                "amq.direct",
                "_test",
                MessageBuilder.withBody(JsonUtil.toBytes(new LoginSysUser())).build()
        );

        System.out.println(JsonUtil.parseObject(message.getBody(), LoginSysUser.class));
    }
}
