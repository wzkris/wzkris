package com.wzkris.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.payment.domain.PayRefundOrderDO;
import com.wzkris.payment.enums.refund.RefundStatusEnum;
import com.wzkris.payment.mapper.PayRefundOrderMapper;
import com.wzkris.payment.service.PayOrderService;
import com.wzkris.payment.service.PayRefundOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class PayRefundOrderServiceImpl
        extends ServiceImplPlus<PayRefundOrderMapper, PayRefundOrderDO>
        implements PayRefundOrderService {

    private final PayOrderService payOrderService;

    @Override
    public boolean updateToSuccess(Long refundOrderId, String channelRefundNo, OffsetDateTime refundAt) {
        return this.update(new LambdaUpdateWrapper<PayRefundOrderDO>()
                .eq(PayRefundOrderDO::getId, refundOrderId)
                .eq(PayRefundOrderDO::getStatus, RefundStatusEnum.REFUNDING)
                .set(PayRefundOrderDO::getStatus, RefundStatusEnum.SUCCESS)
                .set(PayRefundOrderDO::getChannelRefundNo, channelRefundNo)
                .set(PayRefundOrderDO::getRefundAt, refundAt));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateToFailed(Long refundOrderId, String reason) {
        PayRefundOrderDO refund = this.getById(refundOrderId);
        if (refund == null || refund.getStatus() != RefundStatusEnum.REFUNDING) {
            return false;
        }
        boolean updated = this.update(new LambdaUpdateWrapper<PayRefundOrderDO>()
                .eq(PayRefundOrderDO::getId, refundOrderId)
                .eq(PayRefundOrderDO::getStatus, RefundStatusEnum.REFUNDING)
                .set(PayRefundOrderDO::getStatus, RefundStatusEnum.FAILED)
                .set(PayRefundOrderDO::getFailReason, reason));
        if (updated) {
            // 退款失败回退原订单预留的退款额度
            payOrderService.releaseRefund(refund.getPayOrderId(), refund.getRefundAmount());
        }
        return updated;
    }

}
