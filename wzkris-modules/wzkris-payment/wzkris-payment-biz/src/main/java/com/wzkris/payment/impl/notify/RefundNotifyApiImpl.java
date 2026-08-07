package com.wzkris.payment.impl.notify;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.payment.api.notify.RefundNotifyApi;
import com.wzkris.payment.domain.PayChannelConfigDO;
import com.wzkris.payment.domain.RefundOrderDO;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.refund.RefundStatusEnum;
import com.wzkris.payment.event.RefundFinishedEvent;
import com.wzkris.payment.provider.PaymentProvider;
import com.wzkris.payment.provider.PaymentProviderRouter;
import com.wzkris.payment.provider.model.RefundNotifyParseResult;
import com.wzkris.payment.service.PayChannelNotifyService;
import com.wzkris.payment.service.RefundOrderService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 渠道【退款】回调编排：公共流程见 {@link AbstractChannelNotifyApi}，
 * 本类仅注入退款特有的解析与状态机。
 *
 * @author wzkris
 */
@Service
public class RefundNotifyApiImpl extends AbstractChannelNotifyApi<RefundNotifyParseResult> implements RefundNotifyApi {

    private final RefundOrderService refundOrderService;

    private final ApplicationEventPublisher eventPublisher;

    public RefundNotifyApiImpl(PaymentProviderRouter router, PayChannelNotifyService channelNotifyService,
                               RefundOrderService refundOrderService, ApplicationEventPublisher eventPublisher) {
        super(router, channelNotifyService);
        this.refundOrderService = refundOrderService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public String handleRefundNotify(PayChannelEnum channel, Long configId, String body, Map<String, String> headers) {
        return handle(channel, configId, body, headers);
    }

    @Override
    protected RefundNotifyParseResult parse(PaymentProvider provider, String body, Map<String, String> headers,
                                            PayChannelConfigDO config) throws Exception {
        return provider.parseRefundNotify(body, headers, config);
    }

    @Override
    protected ProcessResult process(RefundNotifyParseResult parsed) {
        RefundOrderDO refund = refundOrderService.getOne(new LambdaQueryWrapper<RefundOrderDO>()
                .eq(RefundOrderDO::getRefundNo, parsed.getOutRefundNo()));
        if (refund == null) {
            return ProcessResult.reject("退款单不存在:" + parsed.getOutRefundNo());
        }
        // 已在终态：幂等成功
        if (refund.getStatus() != RefundStatusEnum.REFUNDING) {
            return ProcessResult.ok();
        }
        if (parsed.isRefundSuccess()) {
            boolean updated = refundOrderService.updateToSuccess(
                    refund.getId(), parsed.getChannelNo(), parsed.getRefundAt());
            if (updated) {
                eventPublisher.publishEvent(new RefundFinishedEvent(refund.getId(), refund.getPayOrderId()));
                return ProcessResult.ok();
            }
            // 状态机更新失败（并发已被推进）：按已处理成功
            return ProcessResult.ok();
        }
        boolean failed = refundOrderService.updateToFailed(refund.getId(), parsed.getErrorMsg());
        return failed ? ProcessResult.ok() : ProcessResult.reject("退款状态已变更");
    }

}
