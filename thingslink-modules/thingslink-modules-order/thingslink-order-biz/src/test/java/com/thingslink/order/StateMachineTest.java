package com.thingslink.order;

import com.thingslink.order.domain.ChargingOrder;
import com.thingslink.order.enums.ChargingEvent;
import com.thingslink.order.enums.ChargingStatus;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.Map;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description :
 * @date : 2023/12/1 13:52
 */
@SpringBootTest
public class StateMachineTest {

    @Autowired
    private StateMachine chargingStateMachine;

    @Autowired
    private StateMachinePersister chargingOrderPersister;

    @Test
    public void test() throws Exception {
        chargingStateMachine.startReactively();

        chargingOrderPersister.restore(chargingStateMachine, ChargingStatus.CHARGING.name());

        System.out.println(chargingStateMachine.getState().getId());

        chargingStateMachine.sendEvent(MessageBuilder.createMessage(ChargingEvent.STOP_CHARGE, new MessageHeaders(Map.of("order", new ChargingOrder()))));

        System.out.println(chargingStateMachine.getState().getId());

        System.out.println("-----------------------------");

        chargingOrderPersister.restore(chargingStateMachine, ChargingStatus.NOTPAY.name());

        System.out.println(chargingStateMachine.getState().getId());

        chargingStateMachine.sendEvent(MessageBuilder.createMessage(ChargingEvent.PAY, new MessageHeaders(Map.of("order", new ChargingOrder()))));

        System.out.println(chargingStateMachine.getState().getId());

    }
}
