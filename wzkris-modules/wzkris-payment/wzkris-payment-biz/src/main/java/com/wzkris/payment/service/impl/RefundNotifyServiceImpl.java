package com.wzkris.payment.service.impl;

import com.wzkris.payment.domain.RefundOrderDO;
import com.wzkris.payment.enums.notify.NotifyTypeEnum;
import com.wzkris.payment.enums.refund.RefundStatusEnum;
import com.wzkris.payment.event.RefundFinishedEvent;
import com.wzkris.payment.impl.router.PaymentProviderRouter;
import com.wzkris.payment.provider.model.ProcessResult;
import com.wzkris.payment.provider.model.RefundNotifyResult;
import com.wzkris.payment.service.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * 渠道【退款】异步回调编排实现。
 *
 * @author wzkris
 */
@Service
public class RefundNotifyServiceImpl extends AbsNotifyService<RefundNotifyResult>
        implements RefundNotifyService {

    private final RefundOrderService refundOrderService;

    private final ApplicationEventPublisher eventPublisher;

    public RefundNotifyServiceImpl(PaymentProviderRouter router,
                                   ChannelNotifyLogService channelNotifyLogService,
                                   RefundOrderService refundOrderService,
                                   ApplicationEventPublisher eventPublisher) {
        super(router, channelNotifyLogService);
        this.refundOrderService = refundOrderService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    protected NotifyTypeEnum notifyType() {
        return NotifyTypeEnum.REFUND;
    }

    @Override
    protected ProcessResult process(RefundNotifyResult notifyResult) {
        RefundOrderDO refund = refundOrderService.getOneByObj(RefundOrderDO::getRefundNo, notifyResult.getOutRefundNo());
        if (refund == null) {
            return ProcessResult.reject("退款单不存在:" + notifyResult.getOutRefundNo());
        }
        if (refund.getStatus() != RefundStatusEnum.REFUNDING) {
            return ProcessResult.ok();
        }
        if (notifyResult.isRefundSuccess()) {
            if (refundOrderService.updateToSuccess(refund.getId(), notifyResult.getChannelNo(), notifyResult.getRefundAt())) {
                eventPublisher.publishEvent(new RefundFinishedEvent(refund.getId()));
            }
            return ProcessResult.ok();
        }
        boolean failed = refundOrderService.updateToFailed(refund.getId(), notifyResult.getErrorMsg());
        return failed ? ProcessResult.ok() : ProcessResult.reject("退款状态已变更");
    }

}
