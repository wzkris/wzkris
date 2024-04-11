package com.thingslink.order.statemachine.actions;

import com.thingslink.order.domain.ChargingOrder;
import com.thingslink.order.enums.ChargingEvent;
import com.thingslink.order.enums.ChargingStatus;
import com.thingslink.order.mapper.ChargingOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 取消支付动作
 * @date : 2023/12/4 12:42
 */
@Component
public class CancelPayAction implements Action<ChargingStatus, ChargingEvent> {
    @Autowired
    private ChargingOrderMapper chargingOrderMapper;

    @Override
    public void execute(StateContext<ChargingStatus, ChargingEvent> context) {
        ChargingOrder order = (ChargingOrder) context.getMessageHeader("order");
        order.setChargingStatus(ChargingStatus.CLOSED.name());
        chargingOrderMapper.updateById(order);

        System.out.println("取消订单");
    }
}
