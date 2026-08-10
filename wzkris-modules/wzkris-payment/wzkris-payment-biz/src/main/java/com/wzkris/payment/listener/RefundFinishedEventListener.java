package com.wzkris.payment.listener;

import com.wzkris.payment.event.RefundFinishedEvent;
import com.wzkris.payment.service.NotifyTaskService;
import com.wzkris.payment.service.RefundOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 退款完结事件监听：加载退款单并委托通知流水线（事务外异步执行，不影响回调响应）
 * <p>退款单冗余 order_no/notify_url，通知自包含，无需回溯原订单
 *
 * @author wzkris
 */
@Component
@RequiredArgsConstructor
public class RefundFinishedEventListener {

    private final RefundOrderService refundOrderService;

    private final NotifyTaskService notifyTaskService;

    @EventListener
    @Async
    public void onRefundFinished(RefundFinishedEvent event) {
        notifyTaskService.createAndSend(refundOrderService.getById(event.getRefundOrderId()));
    }

}
