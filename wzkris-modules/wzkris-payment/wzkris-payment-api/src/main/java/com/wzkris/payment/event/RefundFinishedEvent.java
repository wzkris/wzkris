package com.wzkris.payment.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 退款完结事件（退款异步回调落库后投递，驱动业务方退款通知任务）
 *
 * @author wzkris
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundFinishedEvent {

    private Long refundOrderId;

    private Long payOrderId;
}
