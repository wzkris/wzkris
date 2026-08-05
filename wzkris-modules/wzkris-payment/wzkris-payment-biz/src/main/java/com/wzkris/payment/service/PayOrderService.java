package com.wzkris.payment.service;

import com.wzkris.common.orm.plus.IServicePlus;
import com.wzkris.payment.domain.PayOrderDO;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * 支付订单
 *
 * @author wzkris
 */
public interface PayOrderService extends IServicePlus<PayOrderDO> {

    PayOrderDO getByBizTypeAndBizNo(String bizType, String bizNo);

    boolean existByBizTypeAndBizNo(String bizType, String bizNo);

    /**
     * 状态机：PENDING -> SUCCESS（仅 PENDING 可流转，条件更新保证并发安全）
     */
    boolean updateToSuccess(Long payOrderId, String channelOrderNo, OffsetDateTime payAt);

    /**
     * 状态机：PENDING -> CLOSED
     */
    boolean updateToClosed(Long payOrderId);

    /**
     * 状态机：PENDING -> FAILED
     */
    boolean updateToFailed(Long payOrderId, String reason);

    /**
     * 原子预留退款额度：refunded_amount + amount <= amount 且订单 SUCCESS 才成功。
     * 退款超额校验的并发护栏，返回 false 表示超额或订单非成功。
     */
    boolean reserveRefund(Long payOrderId, BigDecimal amount);

    /**
     * 释放预留的退款额度（退款失败回退）
     */
    void releaseRefund(Long payOrderId, BigDecimal amount);

    /**
     * 查询已超时仍未支付的 PENDING 订单（expire_at <= now），按到期时间升序取 limit 条。
     * 供关单巡检任务使用。
     */
    List<PayOrderDO> findExpiredPending(int limit);
}
