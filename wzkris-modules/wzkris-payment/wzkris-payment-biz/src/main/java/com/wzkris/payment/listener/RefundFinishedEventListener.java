package com.wzkris.payment.listener;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.payment.api.notify.PayNotifyRequest;
import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.domain.RefundOrderDO;
import com.wzkris.payment.enums.notify.NotifyTypeEnum;
import com.wzkris.payment.event.RefundFinishedEvent;
import com.wzkris.payment.service.PayOrderService;
import com.wzkris.payment.service.RefundOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 退款完结事件监听：构造退款通知载荷并委托投递（事务外异步执行，不影响回调响应）
 *
 * @author wzkris
 */
@Component
@RequiredArgsConstructor
public class RefundFinishedEventListener {

    private final PayOrderService payOrderService;

    private final RefundOrderService refundOrderService;

    private final NotifyTaskDispatcher dispatcher;

    @EventListener
    @Async
    public void onRefundFinished(RefundFinishedEvent event) {
        PayOrderDO order = payOrderService.getById(event.getPayOrderId());
        if (order == null || StringUtil.isEmpty(order.getNotifyUrl())) {
            return;
        }
        RefundOrderDO refund = refundOrderService.getById(event.getRefundOrderId());
        if (refund == null) {
            return;
        }

        PayNotifyRequest req = new PayNotifyRequest();
        req.setNotifyType(NotifyTypeEnum.REFUND);
        req.setPayOrderId(order.getId());
        req.setOrderNo(order.getOrderNo());
        req.setBizType(order.getBizType());
        req.setBizNo(order.getBizNo());
        req.setChannel(order.getChannel());
        req.setRefundOrderId(refund.getId());
        req.setRefundNo(refund.getRefundNo());
        req.setRefundAmount(refund.getRefundAmount());
        req.setRefundStatus(refund.getStatus());
        req.setChannelRefundNo(refund.getChannelRefundNo());
        req.setRefundAt(refund.getRefundAt());

        dispatcher.dispatch(NotifyTypeEnum.REFUND, order, req, refund.getId());
    }

}
