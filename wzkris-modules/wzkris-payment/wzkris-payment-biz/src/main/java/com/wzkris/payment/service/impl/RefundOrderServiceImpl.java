package com.wzkris.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.payment.domain.RefundOrderDO;
import com.wzkris.payment.enums.refund.RefundStatusEnum;
import com.wzkris.payment.mapper.RefundOrderMapper;
import com.wzkris.payment.service.PayOrderService;
import com.wzkris.payment.service.RefundOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class RefundOrderServiceImpl
        extends ServiceImplPlus<RefundOrderMapper, RefundOrderDO>
        implements RefundOrderService {

    private final PayOrderService payOrderService;

    @Override
    public RefundOrderDO getByRefundNo(String refundNo) {
        return this.getOne(new LambdaQueryWrapper<RefundOrderDO>()
                .eq(RefundOrderDO::getRefundNo, refundNo));
    }

    @Override
    public boolean updateToSuccess(Long refundOrderId, String channelRefundNo, OffsetDateTime refundAt) {
        return this.update(new LambdaUpdateWrapper<RefundOrderDO>()
                .eq(RefundOrderDO::getId, refundOrderId)
                .eq(RefundOrderDO::getStatus, RefundStatusEnum.REFUNDING)
                .set(RefundOrderDO::getStatus, RefundStatusEnum.SUCCESS)
                .set(RefundOrderDO::getChannelRefundNo, channelRefundNo)
                .set(RefundOrderDO::getRefundAt, refundAt));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateToFailed(Long refundOrderId, String reason) {
        RefundOrderDO refund = this.getById(refundOrderId);
        if (refund == null || refund.getStatus() != RefundStatusEnum.REFUNDING) {
            return false;
        }
        boolean updated = this.update(new LambdaUpdateWrapper<RefundOrderDO>()
                .eq(RefundOrderDO::getId, refundOrderId)
                .eq(RefundOrderDO::getStatus, RefundStatusEnum.REFUNDING)
                .set(RefundOrderDO::getStatus, RefundStatusEnum.FAILED)
                .set(RefundOrderDO::getFailReason, reason));
        if (updated) {
            // 退款失败回退原订单预留的退款额度
            payOrderService.releaseRefund(refund.getPayOrderId(), refund.getRefundAmount());
        }
        return updated;
    }

}
