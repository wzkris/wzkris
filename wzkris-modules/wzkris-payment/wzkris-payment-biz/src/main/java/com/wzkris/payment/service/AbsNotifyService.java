package com.wzkris.payment.service;

import com.wzkris.common.redis.util.DistLockTemplate;
import com.wzkris.payment.domain.ChannelNotifyLogDO;
import com.wzkris.payment.domain.PayChannelConfigDO;
import com.wzkris.payment.enums.notify.NotifyTypeEnum;
import com.wzkris.payment.impl.router.PaymentProviderRouter;
import com.wzkris.payment.provider.PayChannelProvider;
import com.wzkris.payment.provider.model.AbsNotifyResult;
import com.wzkris.payment.provider.model.ChannelResult;
import com.wzkris.payment.provider.model.PaymentProviderContext;
import com.wzkris.payment.provider.model.ProcessResult;
import org.springframework.dao.DuplicateKeyException;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 渠道回调编排模板（Service 层）：resolve 配对渠道+商户配置 → 落库 → 验签 → 解析 → 幂等 → 锁内状态机，
 * ACK 由渠道 provider 决定。
 *
 * @author wzkris
 */
public abstract class AbsNotifyService<T extends AbsNotifyResult> {

    protected final PaymentProviderRouter router;

    protected final ChannelNotifyLogService channelNotifyLogService;

    protected AbsNotifyService(PaymentProviderRouter router,
                               ChannelNotifyLogService channelNotifyLogService) {
        this.router = router;
        this.channelNotifyLogService = channelNotifyLogService;
    }

    /**
     * 回调编排模板。
     */
    public final String handle(Long configId, String body, Map<String, String> headers) {
        // 1. 配对渠道+商户配置（渠道由配置派生）。不校验 ENABLED——停用商户的已成交回调仍需结算。
        //    失败无 provider 可构造 ACK，回通用 NACK 促使渠道重试
        ChannelResult<PaymentProviderContext> resolved = router.resolve(configId);
        if (!resolved.success()) {
            return "fail";
        }
        PaymentProviderContext ctx = resolved.data();
        PayChannelProvider provider = ctx.provider();
        PayChannelConfigDO config = ctx.config();

        // 2. 先落库（原始报文），通知类型和业务号后续回填
        ChannelNotifyLogDO current = new ChannelNotifyLogDO();
        current.setChannel(config.getChannel());
        current.setNotifyData(body);
        current.setVerifyResult(false);
        current.setProcessed(false);
        channelNotifyLogService.save(current);

        // 3. 验签回调来源（resolve 后已配对商户配置，验签依赖商户密钥）
        try {
            boolean verified = provider.verifyNotify(body, headers, ctx);

            if (!verified) {
                current.setErrorMsg("验签失败");
                channelNotifyLogService.updateById(current);
                return provider.buildNotifyAck(false);
            }
        } catch (Exception e) {
            current.setErrorMsg("回调验签异常:" + e.getMessage());
            channelNotifyLogService.updateById(current);
            return provider.buildNotifyAck(false);
        }

        // 4. 解析回调（验签已前置，仅解析业务字段），解析失败则返回 NACK
        T parsed;
        try {
            parsed = switch (notifyType()) {
                case PAY -> (T) provider.parsePayNotify(body, ctx);
                case REFUND -> (T) provider.parseRefundNotify(body, ctx);
            };
        } catch (Exception e) {
            current.setErrorMsg("回调解析异常:" + e.getMessage());
            channelNotifyLogService.updateById(current);
            return provider.buildNotifyAck(false);
        }

        NotifyTypeEnum type = parsed.notifyType();
        String outNo = parsed.outBusinessNo();

        // 5. 查已存在记录：processed=true 已处理直接 ACK；processed=false 前序中断需重新推进
        ChannelNotifyLogDO exist = channelNotifyLogService.findByChannelAndTypeAndOutBusinessNo(
                config.getChannel(), type, outNo);
        if (exist != null && Boolean.TRUE.equals(exist.getProcessed())) {
            // 已处理，本笔标记重复后 ACK（快速幂等）
            current.setVerifyResult(true);
            current.setProcessed(true);
            current.setProcessedAt(OffsetDateTime.now());
            current.setErrorMsg("重复回调(已处理)");
            channelNotifyLogService.updateById(current);
            return provider.buildNotifyAck(true);
        }

        // 主记录：持有业务号的那条，process 完更新它的 processed
        ChannelNotifyLogDO mainRecord;
        if (exist != null) {
            // 前序未处理（处理中断）：本笔标记重复留痕，复用 exist 重新推进状态机
            current.setVerifyResult(true);
            current.setProcessed(true);
            current.setProcessedAt(OffsetDateTime.now());
            current.setErrorMsg("重复回调(前序未处理,重新推进)");
            channelNotifyLogService.updateById(current);
            mainRecord = exist;
        } else {
            // 无前序：回填本笔业务号作为主记录，并发冲突按已处理
            current.setNotifyType(type);
            current.setOutBusinessNo(outNo);
            current.setChannelNo(parsed.getChannelNo());
            current.setVerifyResult(true);
            try {
                channelNotifyLogService.updateById(current);
                mainRecord = current;
            } catch (DuplicateKeyException e) {
                // 并发：他人刚占用，按已处理 ACK（他人会推进）
                current.setProcessed(true);
                current.setProcessedAt(OffsetDateTime.now());
                current.setErrorMsg("重复回调(并发)");
                channelNotifyLogService.updateById(current);
                return provider.buildNotifyAck(true);
            }
        }

        // 6. 锁内状态机
        String lockKey = "pay:notify:" + config.getChannel().getValue() + ":" + type.getValue() + ":" + outNo;
        ProcessResult result = DistLockTemplate.lockAndExecute(lockKey,
                (Supplier<ProcessResult>) () -> process(parsed));

        // 状态机结果决定主记录终态与 ACK
        boolean success = switch (result) {
            case ProcessResult.Ok() -> true;
            case ProcessResult.Reject(String reason) -> {
                mainRecord.setErrorMsg(reason);
                yield false;
            }
        };
        mainRecord.setProcessed(success);
        mainRecord.setProcessedAt(OffsetDateTime.now());
        channelNotifyLogService.updateById(mainRecord);

        return provider.buildNotifyAck(success);
    }

    /**
     * 本模板服务的回调类型（PAY/REFUND，由端点决定），子类声明，模板据此调 provider 对应解析方法。
     */
    protected abstract NotifyTypeEnum notifyType();

    /**
     * 锁内状态机处理
     */
    protected abstract ProcessResult process(T notifyResult);

}
