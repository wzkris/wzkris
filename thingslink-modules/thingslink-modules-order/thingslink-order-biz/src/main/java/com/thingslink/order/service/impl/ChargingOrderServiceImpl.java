package com.thingslink.order.service.impl;

import com.thingslink.order.domain.ChargingOrder;
import com.thingslink.order.enums.ChargingEvent;
import com.thingslink.order.enums.ChargingStatus;
import com.thingslink.order.mapper.ChargingOrderMapper;
import com.thingslink.order.service.ChargingOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 订单信息(Order)表服务实现类
 *
 * @author wzkris
 * @since 2023-04-17 16:32:39
 */
@Service
@RequiredArgsConstructor
public class ChargingOrderServiceImpl implements ChargingOrderService {

    private final ChargingOrderMapper chargingOrderMapper;

    private final StateMachine<ChargingStatus, ChargingEvent> chargingStateMachine;

    private final StateMachinePersister<ChargingStatus, ChargingEvent, ChargingStatus> chargingStateMachinePersister;

    @Override
    public boolean changeStatus(ChargingEvent event, ChargingOrder order) {
        try {
            chargingStateMachine.startReactively();

            chargingStateMachinePersister.restore(chargingStateMachine, ChargingStatus.valueOf(order.getChargingStatus()));

            return chargingStateMachine.sendEvent(MessageBuilder.createMessage(event, new MessageHeaders(Map.of("order", order))));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            chargingStateMachine.stopReactively();
        }
        return false;
    }
}
