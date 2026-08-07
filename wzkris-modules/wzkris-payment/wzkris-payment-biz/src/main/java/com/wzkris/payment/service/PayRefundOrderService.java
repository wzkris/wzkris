package com.wzkris.payment.service;

import com.wzkris.common.orm.plus.IServicePlus;
import com.wzkris.payment.domain.PayRefundOrderDO;

import java.time.OffsetDateTime;

/**
 * 退款订单
 *
 * @author wzkris
 */
public interface PayRefundOrderService extends IServicePlus<PayRefundOrderDO> {

    /**
     * 状态机：REFUNDING -> SUCCESS（并发安全，仅退款中可流转）
     */
    boolean updateToSuccess(Long refundOrderId, String channelRefundNo, OffsetDateTime refundAt);

    /**
     * 状态机：REFUNDING -> FAILED（并发安全，仅退款中可流转），并回退原订单预留的退款额度
     */
    boolean updateToFailed(Long refundOrderId, String reason);

}
