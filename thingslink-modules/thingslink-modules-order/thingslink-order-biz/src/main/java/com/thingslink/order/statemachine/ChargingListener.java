package com.thingslink.order.statemachine;

import com.thingslink.order.domain.ChargingOrder;
import com.thingslink.order.enums.ChargingEvent;
import com.thingslink.order.enums.ChargingStatus;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 状态监听器，在状态机action后执行后触发
 * @date : 2023/4/29 8:16
 */
@Component
@WithStateMachine(name = "chargingStateMachine")
public class ChargingListener {

    @OnTransition(source = "NOTPAY", target = "SUCCESS")
    public boolean payTransition(Message<ChargingEvent> message) {
        // 记录支付动作
        System.out.println("支付，状态机反馈信息：" + message.getHeaders());
        return true;
    }

    @OnTransition(source = "CHARGING", target = "CHARGE_END")
    public boolean closeChargeTransition(Message<ChargingEvent> message) {
        // 记录关闭充电动作
        System.out.println("关闭充电，状态机反馈信息：" + message.getHeaders());
        return true;
    }
}

