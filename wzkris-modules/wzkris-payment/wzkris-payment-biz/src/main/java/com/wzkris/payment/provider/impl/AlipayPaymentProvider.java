package com.wzkris.payment.provider.impl;

import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.exception.service.BusinessException;
import com.wzkris.payment.domain.PayChannelConfigDO;
import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.domain.RefundOrderDO;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.provider.PaymentProvider;
import com.wzkris.payment.provider.model.PayNotifyParseResult;
import com.wzkris.payment.provider.model.PayQueryResult;
import com.wzkris.payment.provider.model.PrepayResult;
import com.wzkris.payment.provider.model.RefundNotifyParseResult;
import com.wzkris.payment.provider.model.RefundResult;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 支付宝策略（P2 接入 alipay-sdk 后实现）
 *
 * @author wzkris
 */
@Component
public class AlipayPaymentProvider implements PaymentProvider {

    @Override
    public PayChannelEnum channel() {
        return PayChannelEnum.ALIPAY;
    }

    @Override
    public PrepayResult prepay(PayOrderDO order, PayChannelConfigDO config) {
        throw new BusinessException(BizBaseCodeEnum.REQUEST_ERROR.value(), "支付宝尚未接入(P2)");
    }

    @Override
    public PayQueryResult query(PayOrderDO order, PayChannelConfigDO config) {
        throw new BusinessException(BizBaseCodeEnum.REQUEST_ERROR.value(), "支付宝尚未接入(P2)");
    }

    @Override
    public void close(PayOrderDO order, PayChannelConfigDO config) {
        throw new BusinessException(BizBaseCodeEnum.REQUEST_ERROR.value(), "支付宝尚未接入(P2)");
    }

    @Override
    public RefundResult refund(RefundOrderDO refund, PayChannelConfigDO config) {
        throw new BusinessException(BizBaseCodeEnum.REQUEST_ERROR.value(), "支付宝尚未接入(P2)");
    }

    @Override
    public RefundResult queryRefund(RefundOrderDO refund, PayChannelConfigDO config) {
        throw new BusinessException(BizBaseCodeEnum.REQUEST_ERROR.value(), "支付宝尚未接入(P2)");
    }

    @Override
    public PayNotifyParseResult parsePayNotify(String body, Map<String, String> headers, PayChannelConfigDO config) {
        throw new BusinessException(BizBaseCodeEnum.REQUEST_ERROR.value(), "支付宝尚未接入(P2)");
    }

    @Override
    public RefundNotifyParseResult parseRefundNotify(String body, Map<String, String> headers, PayChannelConfigDO config) {
        throw new BusinessException(BizBaseCodeEnum.REQUEST_ERROR.value(), "支付宝尚未接入(P2)");
    }

    @Override
    public String buildNotifyAck(boolean success) {
        // 支付宝要求返回 "success"
        return success ? "success" : "fail";
    }

}
