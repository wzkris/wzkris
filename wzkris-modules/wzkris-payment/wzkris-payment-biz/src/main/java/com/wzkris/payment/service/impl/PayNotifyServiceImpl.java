package com.wzkris.payment.service.impl;

import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.enums.notify.NotifyTypeEnum;
import com.wzkris.payment.enums.pay.PayStatusEnum;
import com.wzkris.payment.event.PayOrderPaidEvent;
import com.wzkris.payment.impl.router.PaymentProviderRouter;
import com.wzkris.payment.provider.model.PayNotifyResult;
import com.wzkris.payment.provider.model.ProcessResult;
import com.wzkris.payment.service.AbsNotifyService;
import com.wzkris.payment.service.ChannelNotifyLogService;
import com.wzkris.payment.service.PayNotifyService;
import com.wzkris.payment.service.PayOrderService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * 渠道【支付】异步回调编排实现。
 *
 * @author wzkris
 */
@Service
public class PayNotifyServiceImpl extends AbsNotifyService<PayNotifyResult> implements PayNotifyService {

    private final PayOrderService payOrderService;

    private final ApplicationEventPublisher eventPublisher;

    public PayNotifyServiceImpl(PaymentProviderRouter router,
                                ChannelNotifyLogService channelNotifyLogService,
                                PayOrderService payOrderService,
                                ApplicationEventPublisher eventPublisher) {
        super(router, channelNotifyLogService);
        this.payOrderService = payOrderService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    protected NotifyTypeEnum notifyType() {
        return NotifyTypeEnum.PAY;
    }

    @Override
    protected ProcessResult process(PayNotifyResult notifyResult) {
        PayOrderDO order = payOrderService.getOneByObj(PayOrderDO::getOrderNo, notifyResult.getOutTradeNo());
        if (order == null) {
            return ProcessResult.reject("支付订单不存在:" + notifyResult.getOutTradeNo());
        }
        if (order.getStatus() != PayStatusEnum.PENDING) {
            return ProcessResult.ok();
        }
        if (!notifyResult.isPaid()) {
            return ProcessResult.reject("渠道未支付成功");
        }
        if (order.getAmount().compareTo(notifyResult.getAmount()) != 0) {
            return ProcessResult.reject("金额不符");
        }
        if (payOrderService.updateToSuccess(order.getId(), notifyResult.getChannelNo(), notifyResult.getPayAt())) {
            eventPublisher.publishEvent(new PayOrderPaidEvent(order.getId()));
        }
        return ProcessResult.ok();
    }

}
