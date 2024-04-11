package com.thingslink.order.statemachine;

import com.thingslink.order.enums.ChargingEvent;
import com.thingslink.order.enums.ChargingStatus;
import com.thingslink.order.statemachine.actions.CancelPayAction;
import com.thingslink.order.statemachine.actions.OpenChargeAction;
import com.thingslink.order.statemachine.actions.PayAction;
import com.thingslink.order.statemachine.actions.StopChargeAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import java.util.EnumSet;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 交易状态机配置
 * @date : 2023/4/27 11:05
 */
@Configuration
@EnableStateMachine(name = "chargingStateMachine")
public class ChargingOrderMachineConfig extends EnumStateMachineConfigurerAdapter<ChargingStatus, ChargingEvent> {

    @Autowired
    private PayAction payAction;

    @Autowired
    private StopChargeAction stopChargeAction;

    @Autowired
    private OpenChargeAction openChargeAction;

    @Autowired
    private CancelPayAction cancelPayAction;

    @Override
    public void configure(StateMachineStateConfigurer<ChargingStatus, ChargingEvent> states) throws Exception {
        states.withStates()
                .initial(ChargingStatus.NOTPAY)
                .states(EnumSet.allOf(ChargingStatus.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<ChargingStatus, ChargingEvent> transitions) throws Exception {
        transitions
                // 支付
                .withExternal()
                .source(ChargingStatus.NOTPAY).target(ChargingStatus.SUCCESS)
                .event(ChargingEvent.PAY)
                .action(payAction)
                .and()
                // 订单关闭
                .withExternal()
                .source(ChargingStatus.NOTPAY).target(ChargingStatus.CLOSED)
                .event(ChargingEvent.CANCEL_ORDER)
                .action(cancelPayAction)
                .and()
                // 打开充电
                .withExternal()
                .source(ChargingStatus.SUCCESS).target(ChargingStatus.CHARGING)
                .event(ChargingEvent.OPEN_CHARGE)
                .action(openChargeAction)
                .and()
                // 充电结束
                .withExternal()
                .source(ChargingStatus.CHARGING).target(ChargingStatus.CHARGE_END)
                .event(ChargingEvent.STOP_CHARGE)
                .action(stopChargeAction);
    }

    /**
     * 注入StateMachinePersister对象
     */
    @Bean(name = "chargingStateMachinePersister")
    public StateMachinePersister<ChargingStatus, ChargingEvent, ChargingStatus> getPersister() {
        return new DefaultStateMachinePersister<>(new StateMachinePersist<>() {
            @Override
            public void write(StateMachineContext<ChargingStatus, ChargingEvent> context, ChargingStatus contextObj) throws Exception {
                // 不持久化了
            }

            @Override
            public StateMachineContext<ChargingStatus, ChargingEvent> read(ChargingStatus contextObj) throws Exception {
                return new DefaultStateMachineContext<>(contextObj,
                        null, null, null, null, null);
            }
        });
    }
}
