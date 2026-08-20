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
 * 可预期的渠道失败走返回值不抛异常。
 *
 * <p>回调处理分两步、职责分离：{@link #verifyNotify} 前置统一验签来源（微信验签请求头 / 支付宝报文签名），
 * 验签失败返回 false；{@code parseXxxNotify} 只解析业务字段（验签已前置，无需再碰 headers）。
 * 两步失败均以返回值表达，不抛异常；验签失败由编排层回 NACK，解析失败以 {@link AbsNotifyResult#getErrorMsg()}
 * 承载、交由编排层落库并回 NACK。
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
     * 前置验签回调来源（微信验签请求头 / 支付宝报文签名）。验签未通过或验签过程异常返回 false，不抛异常，
     * 由编排层据此记录并回 NACK。
     */
    boolean verifyNotify(String body, Map<String, String> headers, PaymentProviderContext ctx);

    /**
     * 解析【支付】异步回调为业务字段（验签已由 {@link #verifyNotify} 前置，无需 headers）。报文非法/解析失败
     * 在返回结果上标 {@link AbsNotifyResult#getErrorMsg()} 返回，不抛异常。
     */
    PayNotifyResult parsePayNotify(String body, PaymentProviderContext ctx);

    /**
     * 解析【退款】异步回调为业务字段，语义同 {@link #parsePayNotify}。
     */
    RefundNotifyResult parseRefundNotify(String body, PaymentProviderContext ctx);

    /**
     * 构造回渠道的应答（微信 SUCCESS XML / 支付宝 success）
     */
    String buildNotifyAck(boolean success);

}
