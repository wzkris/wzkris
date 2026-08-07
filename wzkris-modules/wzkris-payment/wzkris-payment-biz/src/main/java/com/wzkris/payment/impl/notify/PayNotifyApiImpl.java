package com.wzkris.payment.impl.notify;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.payment.api.notify.PayNotifyApi;
import com.wzkris.payment.domain.PayChannelConfigDO;
import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.pay.PayStatusEnum;
import com.wzkris.payment.event.PayOrderPaidEvent;
import com.wzkris.payment.provider.PaymentProvider;
import com.wzkris.payment.provider.PaymentProviderRouter;
import com.wzkris.payment.provider.model.PayNotifyParseResult;
import com.wzkris.payment.service.PayChannelNotifyService;
import com.wzkris.payment.service.PayOrderService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 渠道【支付】回调编排：公共流程见 {@link AbstractChannelNotifyApi}，
 * 本类仅注入支付特有的解析与状态机。
 *
 * @author wzkris
 */
@Service
public class PayNotifyApiImpl extends AbstractChannelNotifyApi<PayNotifyParseResult> implements PayNotifyApi {

    private final PayOrderService payOrderService;

    private final ApplicationEventPublisher eventPublisher;

    public PayNotifyApiImpl(PaymentProviderRouter router, PayChannelNotifyService channelNotifyService,
                            PayOrderService payOrderService, ApplicationEventPublisher eventPublisher) {
        super(router, channelNotifyService);
        this.payOrderService = payOrderService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public String handlePayNotify(PayChannelEnum channel, Long configId, String body, Map<String, String> headers) {
        return handle(channel, configId, body, headers);
    }

    @Override
    protected PayNotifyParseResult parse(PaymentProvider provider, String body, Map<String, String> headers,
                                         PayChannelConfigDO config) throws Exception {
        return provider.parsePayNotify(body, headers, config);
    }

    @Override
    protected ProcessResult process(PayNotifyParseResult parsed) {
        PayOrderDO order = payOrderService.getOne(new LambdaQueryWrapper<PayOrderDO>()
                .eq(PayOrderDO::getOrderNo, parsed.getOutTradeNo()));
        if (order == null) {
            return ProcessResult.reject("支付订单不存在:" + parsed.getOutTradeNo());
        }
        // 已在终态：幂等成功
        if (order.getStatus() != PayStatusEnum.PENDING) {
            return ProcessResult.ok();
        }
        if (!parsed.isPaid()) {
            return ProcessResult.reject("渠道未支付成功");
        }
        // 金额必须一致，防止篡改
        if (order.getAmount().compareTo(parsed.getAmount()) != 0) {
            return ProcessResult.reject("金额不符");
        }
        boolean updated = payOrderService.updateToSuccess(
                order.getId(), parsed.getChannelNo(), parsed.getPayAt());
        if (updated) {
            eventPublisher.publishEvent(new PayOrderPaidEvent(order.getId(), order.getBizType()));
            return ProcessResult.ok();
        }
        // 状态机更新失败（并发已被推进）：按已处理成功
        return ProcessResult.ok();
    }

}
