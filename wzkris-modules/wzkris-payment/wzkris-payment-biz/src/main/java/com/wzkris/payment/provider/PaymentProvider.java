package com.wzkris.payment.provider;

import com.wzkris.payment.domain.PayChannelConfigDO;
import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.domain.RefundOrderDO;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.provider.model.PayNotifyParseResult;
import com.wzkris.payment.provider.model.PayQueryResult;
import com.wzkris.payment.provider.model.PrepayResult;
import com.wzkris.payment.provider.model.RefundNotifyParseResult;
import com.wzkris.payment.provider.model.RefundResult;

import java.util.Map;

/**
 * 渠道支付策略：每个外部支付商一个实现，核心流程零修改即可扩展新渠道。
 *
 * <p>新增渠道 = 实现本接口 + 在 {@link PayChannelEnum} 加枚举值。
 *
 * @author wzkris
 */
public interface PaymentProvider {

    /**
     * 渠道身份
     */
    PayChannelEnum channel();

    /**
     * 预下单，返回渠道侧支付参数
     */
    PrepayResult prepay(PayOrderDO order, PayChannelConfigDO config);

    /**
     * 主动查单
     */
    PayQueryResult query(PayOrderDO order, PayChannelConfigDO config);

    /**
     * 关单
     */
    void close(PayOrderDO order, PayChannelConfigDO config);

    /**
     * 退款
     */
    RefundResult refund(RefundOrderDO refund, PayChannelConfigDO config);

    /**
     * 查询退款
     */
    RefundResult queryRefund(RefundOrderDO refund, PayChannelConfigDO config);

    /**
     * 解析并验签【支付】异步回调。验签/解密失败时抛出，由编排层 catch 落库并回 NACK。
     */
    PayNotifyParseResult parsePayNotify(String body, Map<String, String> headers, PayChannelConfigDO config) throws Exception;

    /**
     * 解析并验签【退款】异步回调。验签/解密失败时抛出，由编排层 catch 落库并回 NACK。
     */
    RefundNotifyParseResult parseRefundNotify(String body, Map<String, String> headers, PayChannelConfigDO config) throws Exception;

    /**
     * 构造回渠道的应答（微信 SUCCESS XML / 支付宝 success）
     */
    String buildNotifyAck(boolean success);

}
