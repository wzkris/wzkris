package com.wzkris.payment.listener;

import com.wzkris.payment.event.PayOrderPaidEvent;
import com.wzkris.payment.service.NotifyTaskService;
import com.wzkris.payment.service.PayOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 支付成功事件监听：加载订单并委托通知流水线（事务外异步执行，不影响回调响应）
 *
 * @author wzkris
 */
@Component
@RequiredArgsConstructor
public class PayOrderPaidEventListener {

    private final PayOrderService payOrderService;

    private final NotifyTaskService notifyTaskService;

    @EventListener
    @Async
    public void onPaid(PayOrderPaidEvent event) {
        notifyTaskService.createAndSend(payOrderService.getById(event.getPayOrderId()));
    }

}
