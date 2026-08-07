package com.wzkris.payment.provider;

import com.wzkris.payment.api.order.response.PrepayResponse;
import com.wzkris.payment.domain.PayChannelConfigDO;
import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.domain.PayRefundOrderDO;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.provider.model.NotifyParseResult;
import com.wzkris.payment.provider.model.PayQueryResult;
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
    PrepayResponse prepay(PayOrderDO order, PayChannelConfigDO config);

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
    RefundResult refund(PayRefundOrderDO refund, PayChannelConfigDO config);

    /**
     * 查询退款
     */
    RefundResult queryRefund(PayRefundOrderDO refund, PayChannelConfigDO config);

    /**
     * 解析并验签异步回调
     */
    NotifyParseResult parseNotify(String body, Map<String, String> headers, PayChannelConfigDO config);

    /**
     * 构造回渠道的应答（微信 SUCCESS XML / 支付宝 success）
     */
    String buildNotifyAck(boolean success);

}
