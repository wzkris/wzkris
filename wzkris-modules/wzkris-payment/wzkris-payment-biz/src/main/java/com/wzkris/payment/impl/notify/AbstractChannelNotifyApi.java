package com.wzkris.payment.impl.notify;

import com.wzkris.common.redis.util.DistLockTemplate;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.payment.domain.PayChannelConfigDO;
import com.wzkris.payment.domain.PayChannelNotifyDO;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.notify.NotifyTypeEnum;
import com.wzkris.payment.provider.PaymentProvider;
import com.wzkris.payment.provider.PaymentProviderRouter;
import com.wzkris.payment.provider.ProviderContext;
import com.wzkris.payment.provider.model.NotifyParseResult;
import com.wzkris.payment.service.PayChannelNotifyService;
import org.springframework.dao.DuplicateKeyException;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 渠道回调编排基类（模板方法）：封装"先落库 -> 验签/解析 -> 幂等 -> 锁内状态机 -> 应答"公共流程。
 *
 * <p>支付/退款各自的解析、状态机处理由子类实现 {@link #parse}/{@link #process} 注入，
 * 回调类型与幂等业务号由 {@link NotifyParseResult} 子类型自带，公共编排逻辑在此共享。
 *
 * @author wzkris
 */
public abstract class AbstractChannelNotifyApi<T extends NotifyParseResult> extends AbstractApi {

    protected final PaymentProviderRouter router;

    protected final PayChannelNotifyService channelNotifyService;

    protected AbstractChannelNotifyApi(PaymentProviderRouter router, PayChannelNotifyService channelNotifyService) {
        this.router = router;
        this.channelNotifyService = channelNotifyService;
    }

    /**
     * 回调编排模板：先落库 -> 验签/解析 -> 幂等 -> 锁内状态机 -> 应答。
     *
     * <p>原始报文在验签/解析前即落库，确保回调不丢失；回调类型由端点决定（不再靠解析报文嗅探区分）。
     */
    protected String handle(PayChannelEnum channel, Long configId, String body, Map<String, String> headers) {
        ProviderContext ctx = router.resolve(channel, configId);

        // 1. 先落库：原始回调报文立即持久化（验签/解析前），确保回调不丢失
        //    notify_type/out_business_no 待验签解密后回填，验签失败/解析异常时留空
        PayChannelNotifyDO current = new PayChannelNotifyDO();
        current.setChannel(channel);
        current.setNotifyData(body);
        current.setVerifyResult(false);
        current.setProcessed(false);
        channelNotifyService.save(current);

        // 2. 验签 + 解密 + 解析（渠道未接入等可能抛异常，此时原始报文已落库不丢失）
        T parsed;
        try {
            parsed = parse(ctx.provider(), body, headers, ctx.config());
        } catch (Exception e) {
            current.setErrorMsg("回调解析异常:" + e.getMessage());
            channelNotifyService.updateById(current);
            return ctx.provider().buildNotifyAck(false);
        }

        // 3. 验签失败：更新记录后回 NACK
        if (!parsed.isVerifySuccess()) {
            current.setErrorMsg("验签失败");
            channelNotifyService.updateById(current);
            return ctx.provider().buildNotifyAck(false);
        }

        // notifyType / outNo 由解析结果类型提供
        NotifyTypeEnum notifyType = parsed.notifyType();
        String outNo = parsed.outBusinessNo();

        // 4. 幂等：同 key 是否已有记录
        //    已处理 -> 重复 ACK；未处理 -> 复用前序记录锁内重试。本笔原始记录标记为重复（保持空 key 不碰唯一索引）
        PayChannelNotifyDO active;
        PayChannelNotifyDO exist = channelNotifyService.findByChannelAndTypeAndOutBusinessNo(
                channel, notifyType, outNo);
        if (exist != null) {
            current.setVerifyResult(true);
            current.setProcessed(true);
            current.setProcessedAt(OffsetDateTime.now());
            current.setErrorMsg(Boolean.TRUE.equals(exist.getProcessed())
                    ? "重复回调(已处理)" : "重复回调(复用前序记录)");
            channelNotifyService.updateById(current);
            if (Boolean.TRUE.equals(exist.getProcessed())) {
                return ctx.provider().buildNotifyAck(true);
            }
            // 前序记录未处理（上次失败渠道重试）：复用前序记录锁内重试
            active = exist;
        } else {
            // 5. 无前序记录：回填正式 key 落库（并发重复命中唯一索引按已接收 ACK）
            current.setNotifyType(notifyType);
            current.setOutBusinessNo(outNo);
            current.setChannelNo(parsed.getChannelNo());
            current.setVerifyResult(true);
            try {
                channelNotifyService.updateById(current);
            } catch (DuplicateKeyException e) {
                // 并发：另一线程已落同 key 记录，本笔标记重复（清空 key 不碰撞）后 ACK
                current.setNotifyType(null);
                current.setOutBusinessNo(null);
                current.setChannelNo(null);
                current.setProcessed(true);
                current.setProcessedAt(OffsetDateTime.now());
                current.setErrorMsg("重复回调(并发)");
                channelNotifyService.updateById(current);
                return ctx.provider().buildNotifyAck(true);
            }
            active = current;
        }

        // 6. 锁内处理：关联订单/退款单 + 金额校验 + 状态机流转
        ProcessResult result = DistLockTemplate.lockAndExecute(
                "pay:notify:" + channel.getValue() + ":" + notifyType.getValue() + ":" + outNo,
                (Supplier<ProcessResult>) () -> process(parsed));

        boolean success;
        String reason;
        if (result instanceof ProcessResult.Ok) {
            success = true;
            reason = null;
        } else if (result instanceof ProcessResult.Reject reject) {
            success = false;
            reason = reject.reason();
        } else {
            // 锁获取失败等返回 null：按失败处理促使渠道重试
            success = false;
            reason = "锁获取失败,稍后重试";
        }

        active.setProcessed(success);
        active.setProcessedAt(OffsetDateTime.now());
        if (!success) {
            active.setErrorMsg(reason);
        }
        channelNotifyService.updateById(active);
        return ctx.provider().buildNotifyAck(success);
    }

    /** 验签 + 解密 + 解析回调，失败抛出由模板 catch 落库 */
    protected abstract T parse(PaymentProvider provider, String body, Map<String, String> headers,
                               PayChannelConfigDO config) throws Exception;

    /** 锁内状态机处理（关联订单/退款单 + 金额校验 + 事件发布） */
    protected abstract ProcessResult process(T parsed);

}
