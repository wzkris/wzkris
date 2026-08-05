package com.wzkris.payment.listener;

import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.payment.config.PaymentProperties;
import com.wzkris.payment.domain.PayNotifyTaskDO;
import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.enums.notify.NotifyTaskStatusEnum;
import com.wzkris.payment.enums.notify.NotifyTypeEnum;
import com.wzkris.payment.event.PayOrderPaidEvent;
import com.wzkris.payment.api.notify.PayNotifyRequest;
import com.wzkris.payment.service.PayNotifyTaskService;
import com.wzkris.payment.service.PayOrderService;
import com.wzkris.payment.service.PayNotifySenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

/**
 * 支付成功事件监听：构造通知任务并首次投递（事务外异步执行，不影响回调响应）
 *
 * @author wzkris
 */
@Component
@RequiredArgsConstructor
public class PayOrderPaidEventListener {

    private final PayOrderService payOrderService;

    private final PayNotifyTaskService taskService;

    private final PayNotifySenderService sender;

    private final PaymentProperties properties;

    @EventListener
    @Async
    public void onPaid(PayOrderPaidEvent event) {
        PayOrderDO order = payOrderService.getById(event.getPayOrderId());
        if (order == null || StringUtil.isEmpty(order.getNotifyUrl())) {
            return;
        }

        PayNotifyRequest req = new PayNotifyRequest();
        req.setNotifyType(NotifyTypeEnum.PAY);
        req.setPayOrderId(order.getId());
        req.setOrderNo(order.getOrderNo());
        req.setBizType(order.getBizType());
        req.setBizNo(order.getBizNo());
        req.setChannel(order.getChannel());
        req.setAmount(order.getAmount());
        req.setStatus(order.getStatus());
        req.setChannelOrderNo(order.getChannelOrderNo());
        req.setPayAt(order.getPayAt());

        PayNotifyTaskDO task = new PayNotifyTaskDO();
        task.setNotifyType(NotifyTypeEnum.PAY);
        task.setPayOrderId(order.getId());
        task.setBizType(order.getBizType());
        task.setTargetUrl(order.getNotifyUrl());
        task.setPayload(JsonUtil.toJsonString(req));
        task.setRetryCount(0);
        task.setMaxRetry(properties.getNotifyMaxRetry());
        task.setStatus(NotifyTaskStatusEnum.PENDING);
        task.setNextRetryAt(OffsetDateTime.now());
        taskService.save(task);

        sender.send(task);
    }
}
