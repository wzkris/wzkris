package com.wzkris.payment.listener;

import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.payment.config.PaymentProperties;
import com.wzkris.payment.domain.PayNotifyTaskDO;
import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.enums.notify.NotifyTaskStatusEnum;
import com.wzkris.payment.enums.notify.NotifyTypeEnum;
import com.wzkris.payment.service.PayNotifySenderService;
import com.wzkris.payment.service.PayNotifyTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

/**
 * 商户通知任务编排：由订单快照与通知载荷构建任务、落库并完成首次投递。
 * 支付/退款两类通知的差异仅在 notifyType 与载荷，公共的建单+投递流程收敛于此。
 *
 * @author wzkris
 */
@Component
@RequiredArgsConstructor
public class NotifyTaskDispatcher {

    private final PayNotifyTaskService taskService;

    private final PayNotifySenderService sender;

    private final PaymentProperties properties;

    /**
     * 构建通知任务、落库并首次投递。
     *
     * @param notifyType    通知类型
     * @param order         支付订单快照（提供 payOrderId/bizType/notifyUrl）
     * @param payload       通知载荷（序列化为 JSON 存入任务）
     * @param refundOrderId 退款单号；仅退款通知传入，支付通知传 null
     */
    public void dispatch(NotifyTypeEnum notifyType, PayOrderDO order, Object payload, Long refundOrderId) {
        PayNotifyTaskDO task = new PayNotifyTaskDO();
        task.setNotifyType(notifyType);
        task.setPayOrderId(order.getId());
        task.setRefundOrderId(refundOrderId);
        task.setBizType(order.getBizType());
        task.setTargetUrl(order.getNotifyUrl());
        task.setPayload(JsonUtil.toJsonString(payload));
        task.setRetryCount(0);
        task.setMaxRetry(properties.getNotifyMaxRetry());
        task.setStatus(NotifyTaskStatusEnum.PENDING);
        task.setNextRetryAt(OffsetDateTime.now());
        taskService.save(task);

        sender.send(task);
    }

}
