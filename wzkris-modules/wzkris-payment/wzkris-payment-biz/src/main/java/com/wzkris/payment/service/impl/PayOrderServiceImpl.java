package com.wzkris.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.enums.pay.PayStatusEnum;
import com.wzkris.payment.mapper.PayOrderMapper;
import com.wzkris.payment.service.PayOrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class PayOrderServiceImpl extends ServiceImplPlus<PayOrderMapper, PayOrderDO> implements PayOrderService {

    @Override
    public PayOrderDO getByBizTypeAndBizNo(String bizType, String bizNo) {
        return this.getOne(new LambdaQueryWrapper<PayOrderDO>()
                .eq(PayOrderDO::getBizType, bizType)
                .eq(PayOrderDO::getBizNo, bizNo));
    }

    @Override
    public boolean existByBizTypeAndBizNo(String bizType, String bizNo) {
        return baseMapper.exists(new LambdaQueryWrapper<PayOrderDO>()
                .eq(PayOrderDO::getBizType, bizType)
                .eq(PayOrderDO::getBizNo, bizNo));
    }

    @Override
    public boolean updateToSuccess(Long payOrderId, String channelOrderNo, OffsetDateTime payAt) {
        return this.update(new LambdaUpdateWrapper<PayOrderDO>()
                .eq(PayOrderDO::getId, payOrderId)
                .eq(PayOrderDO::getStatus, PayStatusEnum.PENDING)
                .set(PayOrderDO::getStatus, PayStatusEnum.SUCCESS)
                .set(PayOrderDO::getChannelOrderNo, channelOrderNo)
                .set(PayOrderDO::getPayAt, payAt));
    }

    @Override
    public boolean updateToClosed(Long payOrderId) {
        return this.update(new LambdaUpdateWrapper<PayOrderDO>()
                .eq(PayOrderDO::getId, payOrderId)
                .eq(PayOrderDO::getStatus, PayStatusEnum.PENDING)
                .set(PayOrderDO::getStatus, PayStatusEnum.CLOSED));
    }

    @Override
    public boolean updateToFailed(Long payOrderId, String reason) {
        return this.update(new LambdaUpdateWrapper<PayOrderDO>()
                .eq(PayOrderDO::getId, payOrderId)
                .eq(PayOrderDO::getStatus, PayStatusEnum.PENDING)
                .set(PayOrderDO::getStatus, PayStatusEnum.FAILED)
                .set(PayOrderDO::getFailReason, reason));
    }

    @Override
    public boolean reserveRefund(Long payOrderId, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            return false;
        }
        return baseMapper.reserveRefund(payOrderId, amount) > 0;
    }

    @Override
    public void releaseRefund(Long payOrderId, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            return;
        }
        baseMapper.releaseRefund(payOrderId, amount);
    }

    @Override
    public List<PayOrderDO> findExpiredPending(int limit) {
        return this.list(new LambdaQueryWrapper<PayOrderDO>()
                .eq(PayOrderDO::getStatus, PayStatusEnum.PENDING)
                .le(PayOrderDO::getExpireAt, OffsetDateTime.now())
                .orderByAsc(PayOrderDO::getExpireAt)
                .last("LIMIT " + limit));
    }

}
