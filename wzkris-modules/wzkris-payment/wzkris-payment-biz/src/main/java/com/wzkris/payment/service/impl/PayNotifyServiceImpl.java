package com.wzkris.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.redis.util.DistLockTemplate;
import com.wzkris.payment.domain.PayChannelNotifyDO;
import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.domain.PayRefundOrderDO;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.notify.NotifyTypeEnum;
import com.wzkris.payment.enums.pay.PayStatusEnum;
import com.wzkris.payment.enums.refund.RefundStatusEnum;
import com.wzkris.payment.event.PayOrderPaidEvent;
import com.wzkris.payment.event.PayRefundFinishedEvent;
import com.wzkris.payment.provider.PaymentProviderRouter;
import com.wzkris.payment.provider.ProviderContext;
import com.wzkris.payment.provider.model.NotifyParseResult;
import com.wzkris.payment.service.PayChannelNotifyService;
import com.wzkris.payment.service.PayOrderService;
import com.wzkris.payment.service.PayRefundOrderService;
import com.wzkris.payment.service.PayNotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 渠道回调编排：验签 -> 幂等 -> 锁内状态机 -> 发布事件
 *
 * <p>按 notifyType 分流支付/退款回调。
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class PayNotifyServiceImpl implements PayNotifyService {

    private final PaymentProviderRouter router;

    private final PayChannelNotifyService channelNotifyService;

    private final PayOrderService payOrderService;

    private final PayRefundOrderService refundOrderService;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public String handleNotify(PayChannelEnum channel, Long configId, String body, Map<String, String> headers) {
        ProviderContext ctx = router.resolve(channel, configId);
        NotifyParseResult parsed = ctx.getProvider().parseNotify(body, headers, ctx.getConfig());

        // 验签失败：落审计记录（notify_type/out_business_no 可为空）后回 NACK
        if (!parsed.isVerifySuccess()) {
            saveRecord(channel, parsed, body, false, "验签失败");
            return ctx.getProvider().buildNotifyAck(false);
        }

        // 幂等：已处理过的回调直接 ACK
        String outNo = outBusinessNo(parsed);
        PayChannelNotifyDO exist = channelNotifyService.findByChannelAndTypeAndOutBusinessNo(
                channel, parsed.getNotifyType(), outNo);
        if (exist != null && Boolean.TRUE.equals(exist.getProcessed())) {
            return ctx.getProvider().buildNotifyAck(true);
        }

        // 落回调记录（验签通过）；并发重复回调命中唯一索引时按已接收 ACK，不再抛 500
        PayChannelNotifyDO record;
        if (exist == null) {
            try {
                record = saveRecord(channel, parsed, body, true, null);
            } catch (DuplicateKeyException e) {
                // 另一线程已落记录并在处理，直接 ACK
                return ctx.getProvider().buildNotifyAck(true);
            }
        } else {
            // 已有记录但未处理（如上次处理失败渠道重试）：复用记录，锁内重试
            record = exist;
        }

        // 锁内处理：关联订单/退款单 + 金额校验 + 状态机流转
        Boolean ok = DistLockTemplate.lockAndExecute(
                "pay:notify:" + channel.getValue() + ":" + parsed.getNotifyType().getValue() + ":" + outNo,
                (Supplier<Boolean>) () -> process(parsed));
        boolean success = Boolean.TRUE.equals(ok);

        record.setProcessed(success);
        record.setProcessedAt(OffsetDateTime.now());
        if (!success) {
            record.setErrorMsg("业务处理失败:订单/退款单不存在或状态不符");
        }
        channelNotifyService.updateById(record);
        return ctx.getProvider().buildNotifyAck(success);
    }

    private String outBusinessNo(NotifyParseResult parsed) {
        return parsed.getNotifyType() == NotifyTypeEnum.PAY ? parsed.getOutTradeNo() : parsed.getOutRefundNo();
    }

    private boolean process(NotifyParseResult parsed) {
        return parsed.getNotifyType() == NotifyTypeEnum.PAY ? processPaid(parsed) : processRefund(parsed);
    }

    private boolean processPaid(NotifyParseResult parsed) {
        PayOrderDO order = payOrderService.getOne(new LambdaQueryWrapper<PayOrderDO>()
                .eq(PayOrderDO::getOrderNo, parsed.getOutTradeNo()));
        if (order == null) {
            return false;
        }
        if (order.getStatus() != PayStatusEnum.PENDING) {
            return true;
        }
        if (!parsed.isPaid()) {
            return false;
        }
        // 金额必须一致，防止篡改
        if (order.getAmount().compareTo(parsed.getAmount()) != 0) {
            return false;
        }
        boolean updated = payOrderService.updateToSuccess(
                order.getPayOrderId(), parsed.getChannelNo(), parsed.getPayAt());
        if (updated) {
            eventPublisher.publishEvent(new PayOrderPaidEvent(order.getPayOrderId(), order.getBizType()));
        }
        return updated;
    }

    private boolean processRefund(NotifyParseResult parsed) {
        PayRefundOrderDO refund = refundOrderService.getOne(new LambdaQueryWrapper<PayRefundOrderDO>()
                .eq(PayRefundOrderDO::getRefundNo, parsed.getOutRefundNo()));
        if (refund == null) {
            return false;
        }
        if (refund.getStatus() != RefundStatusEnum.REFUNDING) {
            return true;
        }
        if (parsed.isRefundSuccess()) {
            boolean updated = refundOrderService.updateToSuccess(
                    refund.getRefundOrderId(), parsed.getChannelNo(), parsed.getRefundAt());
            if (updated) {
                eventPublisher.publishEvent(new PayRefundFinishedEvent(refund.getRefundOrderId(), refund.getPayOrderId()));
            }
            return updated;
        }
        return refundOrderService.updateToFailed(refund.getRefundOrderId(), parsed.getErrorMsg());
    }

    private PayChannelNotifyDO saveRecord(PayChannelEnum channel, NotifyParseResult parsed,
                                          String body, boolean verify, String err) {
        PayChannelNotifyDO record = new PayChannelNotifyDO();
        record.setChannel(channel);
        record.setNotifyType(parsed.getNotifyType());
        record.setOutBusinessNo(outBusinessNo(parsed));
        record.setChannelNo(parsed.getChannelNo());
        record.setNotifyData(body);
        record.setVerifyResult(verify);
        record.setProcessed(false);
        record.setErrorMsg(err);
        channelNotifyService.save(record);
        return record;
    }
}
