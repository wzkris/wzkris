package com.wzkris.payment.impl.refund;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.payment.api.refund.PayRefundApi;
import com.wzkris.payment.api.refund.request.RefundApplyRequest;
import com.wzkris.payment.api.refund.response.RefundOrderResponse;
import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.domain.PayRefundOrderDO;
import com.wzkris.payment.enums.pay.PayStatusEnum;
import com.wzkris.payment.enums.refund.RefundStatusEnum;
import com.wzkris.payment.event.PayRefundFinishedEvent;
import com.wzkris.payment.provider.PaymentProviderRouter;
import com.wzkris.payment.provider.ProviderContext;
import com.wzkris.payment.provider.model.RefundResult;
import com.wzkris.payment.service.PayOrderService;
import com.wzkris.payment.service.PayRefundOrderService;
import com.wzkris.payment.util.OrderNoGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 退款
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class PayRefundApiImpl extends AbstractApi implements PayRefundApi {

    private final PayOrderService payOrderService;

    private final PayRefundOrderService refundOrderService;

    private final PaymentProviderRouter router;

    private final OrderNoGenerator orderNoGenerator;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Result<RefundOrderResponse> apply(RefundApplyRequest request) {
        PayOrderDO order = payOrderService.getById(request.getPayOrderId());
        if (order == null) {
            return requestFail("支付订单不存在");
        }
        if (order.getStatus() != PayStatusEnum.SUCCESS) {
            return requestFail("原订单未支付成功");
        }
        // 预校验可退余额（非原子，仅挡明显超额；真正护栏是下面的 reserveRefund）
        BigDecimal refunded = order.getRefundedAmount() == null ? BigDecimal.ZERO : order.getRefundedAmount();
        if (refunded.add(request.getRefundAmount()).compareTo(order.getAmount()) > 0) {
            return requestFail("退款金额超过可退金额");
        }

        // 原子预留退款额度：refunded_amount + 本次 <= amount，并发安全护栏
        if (!payOrderService.reserveRefund(order.getId(), request.getRefundAmount())) {
            return requestFail("退款金额超过可退金额");
        }

        // 建退款单(REFUNDING)，短事务提交；建单失败则回退已预留额度
        PayRefundOrderDO refund = new PayRefundOrderDO();
        refund.setRefundNo(orderNoGenerator.nextRefundNo());
        refund.setPayOrderId(order.getId());
        refund.setChannel(order.getChannel());
        refund.setConfigId(order.getConfigId());
        refund.setRefundAmount(request.getRefundAmount());
        refund.setReason(request.getReason());
        refund.setStatus(RefundStatusEnum.REFUNDING);
        try {
            refundOrderService.save(refund);
        } catch (RuntimeException e) {
            payOrderService.releaseRefund(order.getId(), request.getRefundAmount());
            throw e;
        }

        // 渠道退款置于事务外：避免外部 HTTP 调用嵌在 DB 事务内，导致渠道已退而本地回滚的不一致
        try {
            ProviderContext ctx = router.resolve(order.getChannel(), order.getConfigId());
            RefundResult result = ctx.provider().refund(refund, ctx.config());
            switch (result.getStatus()) {
                case SUCCESS -> {
                    boolean updated = refundOrderService.updateToSuccess(
                            refund.getId(), result.getChannelRefundNo(), result.getRefundAt());
                    if (updated) {
                        // 同步退款成功同样发布事件驱动业务方通知，与异步回调路径一致
                        eventPublisher.publishEvent(
                                new PayRefundFinishedEvent(refund.getId(), order.getId()));
                    }
                }
                case PROCESSING -> {
                    // 渠道已受理退款(微信异步退款)，保持 REFUNDING，等异步退款回调终结为 SUCCESS/FAILED
                }
                case FAILED -> refundOrderService.updateToFailed(refund.getId(), result.getErrorMsg());
            }
        } catch (Exception e) {
            refundOrderService.updateToFailed(refund.getId(), e.getMessage());
        }

        PayRefundOrderDO latest = refundOrderService.getById(refund.getId());
        RefundOrderResponse resp = BeanCopierUtil.copy(latest, RefundOrderResponse.class);
        // 枚举字段不被 cglib 自动拷贝，手动赋值
        resp.setStatus(latest.getStatus());
        resp.setChannel(latest.getChannel());
        return ok(resp);
    }

    @Override
    public Result<RefundOrderResponse> queryById(IdRequest request) {
        PayRefundOrderDO refund = refundOrderService.getById(request.getId());
        return ok(BeanCopierUtil.copy(refund, RefundOrderResponse.class));
    }

}
