package com.wzkris.payment.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 支付订单支付成功事件（进程内解耦：支付状态落库后投递业务方通知任务）
 *
 * @author wzkris
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayOrderPaidEvent {

    private Long payOrderId;

    private String bizType;
}
