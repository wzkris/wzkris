package com.wzkris.payment.provider;

import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.domain.RefundOrderDO;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.provider.model.*;

import java.util.Map;

/**
 * 渠道支付策略：每个外部支付商一个实现，核心流程零修改即可扩展新渠道。
 *
 * <p>主动渠道调用(prepay/query/close/refund/queryRefund)以 {@link ChannelResult} 返回成功/失败,
 * 可预期的渠道失败走返回值不抛异常;仅回调 {@code parseXxxNotify} 的解密/解析异常才抛出(由编排层兜底)。
 *
 * <p>新增渠道 = 实现本接口 + 在 {@link PayChannelEnum} 加枚举值。
 *
 * @author wzkris
 */
public interface PayChannelProvider {

    /**
     * 渠道身份
     */
    PayChannelEnum channel();

    /**
     * 预下单，返回渠道侧支付参数。商户配置由 {@link PaymentProviderContext} 携带，尽早配对后透传，不再分别传双参。
     */
    ChannelResult<PrepayResult> prepay(PayOrderDO order, PaymentProviderContext ctx);

    /**
     * 主动查单
     */
    ChannelResult<PayQueryResult> query(PayOrderDO order, PaymentProviderContext ctx);

    /**
     * 关单
     */
    ChannelResult<Void> close(PayOrderDO order, PaymentProviderContext ctx);

    /**
     * 退款（需原支付单总额，由调用方传入避免重复回查）
     */
    ChannelResult<RefundResult> refund(RefundOrderDO refund, PayOrderDO order, PaymentProviderContext ctx);

    /**
     * 查询退款
     */
    ChannelResult<RefundResult> queryRefund(RefundOrderDO refund, PaymentProviderContext ctx);

    /**
     * 解析【支付】异步回调体为业务字段（验签已由 {@link #verifyNotify} 前置，此处仅解析）。解析失败时抛出，由编排层 catch 落库并回 NACK。
     */
    PayNotifyResult parsePayNotify(String body, PaymentProviderContext ctx) throws Exception;

    /**
     * 解析【退款】异步回调体为业务字段（验签已由 {@link #verifyNotify} 前置，此处仅解析）。解析失败时抛出，由编排层 catch 落库并回 NACK。
     */
    RefundNotifyResult parseRefundNotify(String body, PaymentProviderContext ctx) throws Exception;

    /**
     * 验签回调来源（微信验签头/支付宝报文签名），由编排层在解析前统一调用。验签失败返回 false，验签过程异常抛出。
     */
    boolean verifyNotify(String body, Map<String, String> headers, PaymentProviderContext ctx) throws Exception;

    /**
     * 构造回渠道的应答（微信 SUCCESS XML / 支付宝 success）
     */
    String buildNotifyAck(boolean success);

}
