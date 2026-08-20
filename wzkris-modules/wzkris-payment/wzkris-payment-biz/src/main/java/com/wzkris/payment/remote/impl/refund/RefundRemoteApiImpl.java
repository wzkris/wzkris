// com.wzkris.payment.remote.impl.refund.RefundRemoteApiImpl.java
package com.wzkris.payment.remote.impl.refund;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.domain.RefundOrderDO;
import com.wzkris.payment.enums.pay.PayStatusEnum;
import com.wzkris.payment.enums.refund.RefundStatusEnum;
import com.wzkris.payment.event.RefundFinishedEvent;
import com.wzkris.payment.impl.router.PaymentProviderRouter;
import com.wzkris.payment.provider.model.ChannelResult;
import com.wzkris.payment.provider.model.PaymentProviderContext;
import com.wzkris.payment.provider.model.RefundResult;
import com.wzkris.payment.remote.api.refund.RefundRemoteApi;
import com.wzkris.payment.remote.api.refund.request.RefundApplyRequest;
import com.wzkris.payment.remote.api.refund.request.RefundNoQueryRequest;
import com.wzkris.payment.remote.api.refund.response.RefundQueryResponse;
import com.wzkris.payment.service.PayOrderService;
import com.wzkris.payment.service.RefundOrderService;
import com.wzkris.payment.util.OrderNoGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RefundRemoteApiImpl extends AbstractApi implements RefundRemoteApi {

    private final PayOrderService payOrderService;

    private final RefundOrderService refundOrderService;

    private final PaymentProviderRouter router;

    private final OrderNoGenerator orderNoGenerator;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Result<RefundQueryResponse> apply(RefundApplyRequest request) {
        PayOrderDO order = payOrderService.getByOrderNo(request.getOrderNo());
        if (order == null) {
            return requestFail("支付订单不存在");
        }
        if (order.getStatus() != PayStatusEnum.SUCCESS) {
            return requestFail("原订单未支付成功");
        }

        // 预校验可退余额
        BigDecimal refunded = order.getRefundedAmount() == null ? BigDecimal.ZERO : order.getRefundedAmount();
        if (refunded.add(request.getRefundAmount()).compareTo(order.getAmount()) > 0) {
            return requestFail("退款金额超过可退金额");
        }

        // 原子预留退款额度
        if (!payOrderService.reserveRefund(order.getId(), request.getRefundAmount())) {
            return requestFail("退款金额超过可退金额");
        }

        // 配对渠道+商户配置（失败需回滚已预留额度）
        ChannelResult<PaymentProviderContext> resolved = router.resolve(order.getConfigId());
        if (!resolved.success()) {
            payOrderService.releaseRefund(order.getId(), request.getRefundAmount());
            return requestFail(resolved.errMsg());
        }
        PaymentProviderContext ctx = resolved.data();

        // 创建退款单（冗余快照 order_no/notify_url，退款通知自包含）
        RefundOrderDO refund = new RefundOrderDO();
        refund.setRefundNo(orderNoGenerator.nextRefundNo());
        refund.setPayOrderId(order.getId());
        refund.setOrderNo(order.getOrderNo());
        refund.setChannel(order.getChannel());
        refund.setConfigId(order.getConfigId());
        refund.setRefundAmount(request.getRefundAmount());
        refund.setReason(request.getReason());
        refund.setNotifyUrl(request.getNotifyUrl());
        refund.setStatus(RefundStatusEnum.REFUNDING);
        try {
            refundOrderService.save(refund);
        } catch (RuntimeException e) {
            payOrderService.releaseRefund(order.getId(), request.getRefundAmount());
            throw e;
        }

        // 调用渠道退款
        ChannelResult<RefundResult> refundResult = ctx.provider().refund(refund, order, ctx);
        if (!refundResult.success()) {
            return finishFailed(refund, refundResult.errMsg());
        }

        RefundResult result = refundResult.data();
        switch (result.getStatus()) {
            case SUCCESS -> {
                boolean updated = refundOrderService.updateToSuccess(
                        refund.getId(), result.getChannelRefundNo(), result.getRefundAt());
                if (updated) {
                    eventPublisher.publishEvent(new RefundFinishedEvent(refund.getId()));
                }
                refund.setStatus(RefundStatusEnum.SUCCESS);
                refund.setChannelRefundNo(result.getChannelRefundNo());
                refund.setRefundAt(result.getRefundAt());
            }
            case REFUNDING -> {
                // 保持 REFUNDING，等待异步回调
            }
            case FAILED -> {
                return finishFailed(refund, result.getErrorMsg());
            }
        }

        return buildResp(refund);
    }

    @Override
    public Result<RefundQueryResponse> queryByRefundNo(RefundNoQueryRequest request) {
        RefundOrderDO refund = refundOrderService.getByRefundNo(request.getRefundNo());
        return buildResp(refund);
    }

    private Result<RefundQueryResponse> finishFailed(RefundOrderDO refund, String reason) {
        refundOrderService.updateToFailed(refund.getId(), reason);
        refund.setStatus(RefundStatusEnum.FAILED);
        refund.setFailReason(reason);
        return buildResp(refund);
    }

    private Result<RefundQueryResponse> buildResp(RefundOrderDO refund) {
        RefundQueryResponse resp = BeanCopierUtil.copy(refund, RefundQueryResponse.class);
        resp.setStatus(refund.getStatus());
        resp.setChannel(refund.getChannel());
        return ok(resp);
    }

}
