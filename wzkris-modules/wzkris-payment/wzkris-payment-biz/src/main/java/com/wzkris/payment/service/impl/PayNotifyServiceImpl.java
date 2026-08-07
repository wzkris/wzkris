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
import com.wzkris.payment.service.PayNotifyService;
import com.wzkris.payment.service.PayOrderService;
import com.wzkris.payment.service.PayRefundOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 渠道回调编排：先落库 -> 验签 -> 幂等 -> 锁内状态机 -> 发布事件
 *
 * <p>原始报文在验签/解析前即落库，确保回调不丢失；按 notifyType 分流支付/退款回调。
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

        // 1. 先落库：原始回调报文立即持久化（验签/解析前），确保回调不丢失
        //    notify_type/out_business_no 待验签解密后回填，验签失败/解析异常时留空
        PayChannelNotifyDO record = new PayChannelNotifyDO();
        record.setChannel(channel);
        record.setNotifyData(body);
        record.setVerifyResult(false);
        record.setProcessed(false);
        channelNotifyService.save(record);

        // 2. 验签 + 解密 + 解析（渠道未接入等可能抛异常，此时原始报文已落库不丢失）
        NotifyParseResult parsed;
        try {
            parsed = ctx.provider().parseNotify(body, headers, ctx.config());
        } catch (Exception e) {
            record.setErrorMsg("回调解析异常:" + e.getMessage());
            channelNotifyService.updateById(record);
            return ctx.provider().buildNotifyAck(false);
        }

        // 3. 验签失败：更新记录后回 NACK
        if (!parsed.isVerifySuccess()) {
            record.setErrorMsg("验签失败");
            channelNotifyService.updateById(record);
            return ctx.provider().buildNotifyAck(false);
        }

        String outNo = outBusinessNo(parsed);
        NotifyTypeEnum notifyType = parsed.getNotifyType();

        // 4. 幂等：同 key 是否已有记录
        //    已处理 -> 重复 ACK；未处理 -> 复用前序记录锁内重试。本笔原始记录标记为重复（保持空 key 不碰唯一索引）
        PayChannelNotifyDO exist = channelNotifyService.findByChannelAndTypeAndOutBusinessNo(
                channel, notifyType, outNo);
        if (exist != null) {
            record.setVerifyResult(true);
            record.setProcessed(true);
            record.setProcessedAt(OffsetDateTime.now());
            record.setErrorMsg(Boolean.TRUE.equals(exist.getProcessed())
                    ? "重复回调(已处理)" : "重复回调(复用前序记录)");
            channelNotifyService.updateById(record);
            if (Boolean.TRUE.equals(exist.getProcessed())) {
                return ctx.provider().buildNotifyAck(true);
            }
            // 前序记录未处理（上次失败渠道重试）：复用前序记录锁内重试
            record = exist;
        } else {
            // 5. 无前序记录：回填正式 key 落库（并发重复命中唯一索引按已接收 ACK）
            record.setNotifyType(notifyType);
            record.setOutBusinessNo(outNo);
            record.setChannelNo(parsed.getChannelNo());
            record.setVerifyResult(true);
            try {
                channelNotifyService.updateById(record);
            } catch (DuplicateKeyException e) {
                // 并发：另一线程已落同 key 记录，本笔标记重复（清空 key 不碰撞）后 ACK
                record.setNotifyType(null);
                record.setOutBusinessNo(null);
                record.setChannelNo(null);
                record.setProcessed(true);
                record.setProcessedAt(OffsetDateTime.now());
                record.setErrorMsg("重复回调(并发)");
                channelNotifyService.updateById(record);
                return ctx.provider().buildNotifyAck(true);
            }
        }

        // 6. 锁内处理：关联订单/退款单 + 金额校验 + 状态机流转
        Boolean ok = DistLockTemplate.lockAndExecute(
                "pay:notify:" + channel.getValue() + ":" + notifyType.getValue() + ":" + outNo,
                (Supplier<Boolean>) () -> process(parsed));
        boolean success = Boolean.TRUE.equals(ok);

        record.setProcessed(success);
        record.setProcessedAt(OffsetDateTime.now());
        if (!success) {
            record.setErrorMsg("业务处理失败:订单/退款单不存在或状态不符");
        }
        channelNotifyService.updateById(record);
        return ctx.provider().buildNotifyAck(success);
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
                order.getId(), parsed.getChannelNo(), parsed.getPayAt());
        if (updated) {
            eventPublisher.publishEvent(new PayOrderPaidEvent(order.getId(), order.getBizType()));
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
                    refund.getId(), parsed.getChannelNo(), parsed.getRefundAt());
            if (updated) {
                eventPublisher.publishEvent(new PayRefundFinishedEvent(refund.getId(), refund.getPayOrderId()));
            }
            return updated;
        }
        return refundOrderService.updateToFailed(refund.getId(), parsed.getErrorMsg());
    }

}
