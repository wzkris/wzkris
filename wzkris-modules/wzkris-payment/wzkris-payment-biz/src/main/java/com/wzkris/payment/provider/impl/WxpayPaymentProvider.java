package com.wzkris.payment.provider.impl;

import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.v3.util.PemUtils;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.exception.service.BusinessException;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.payment.domain.PayChannelConfigDO;
import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.domain.RefundOrderDO;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.pay.PayModeEnum;
import com.wzkris.payment.provider.PaymentProvider;
import com.wzkris.payment.provider.model.PayNotifyParseResult;
import com.wzkris.payment.provider.model.PayQueryResult;
import com.wzkris.payment.provider.model.PrepayResult;
import com.wzkris.payment.provider.model.RefundNotifyParseResult;
import com.wzkris.payment.provider.model.RefundResult;
import com.wzkris.payment.provider.model.RefundResultStatus;
import com.wzkris.payment.provider.wxpay.WxPayServiceFactory;
import com.wzkris.payment.service.PayOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 微信支付策略（weixin-java-pay 4.6.7.B v3 API）
 *
 * <p>退款为受理制：refund() 返回 PROCESSING 不立即置 SUCCESS，由异步退款回调终结。
 *
 * @author wzkris
 */
@Component
@RequiredArgsConstructor
public class WxpayPaymentProvider implements PaymentProvider {

    private final WxPayServiceFactory factory;

    private final PayOrderService payOrderService;

    @Override
    public PayChannelEnum channel() {
        return PayChannelEnum.WXPAY;
    }

    @Override
    public PrepayResult prepay(PayOrderDO order, PayChannelConfigDO config) {
        WxPayService service = factory.get(config);
        WxPayUnifiedOrderV3Request req = new WxPayUnifiedOrderV3Request();
        req.setOutTradeNo(order.getOrderNo());
        req.setDescription(order.getSubject());
        req.setNotifyUrl(config.getNotifyUrl());
        if (order.getExpireAt() != null) {
            req.setTimeExpire(order.getExpireAt().toString());
        }
        req.setAmount(new WxPayUnifiedOrderV3Request.Amount().setTotal(toFen(order.getAmount())));
        if (order.getPayMode() == PayModeEnum.JSAPI) {
            if (order.getPayerId() == null) {
                throw new BusinessException(BizBaseCodeEnum.REQUEST_ERROR.value(), "JSAPI支付需提供payerId(微信open_id)");
            }
            req.setPayer(new WxPayUnifiedOrderV3Request.Payer().setOpenid(order.getPayerId()));
        }
        TradeTypeEnum tradeType = mapTradeType(order.getPayMode());
        WxPayUnifiedOrderV3Result result;
        try {
            result = service.unifiedOrderV3(tradeType, req);
        } catch (WxPayException e) {
            throw new BusinessException(BizBaseCodeEnum.REQUEST_ERROR.value(), "微信预下单失败:" + e.getMessage());
        }
        return new PrepayResult(buildPrepayPayload(tradeType, config, result));
    }

    /**
     * 按 payMode 取渠道侧支付参数：NATIVE=code_url、H5=h5_url、JSAPI/APP=二次签名后的唤起参数JSON。
     */
    private String buildPrepayPayload(TradeTypeEnum tradeType, PayChannelConfigDO config,
                                      WxPayUnifiedOrderV3Result result) {
        return switch (tradeType) {
            case NATIVE -> result.getCodeUrl();
            case H5 -> result.getH5Url();
            case JSAPI, APP -> {
                Object info = result.getPayInfo(tradeType, config.getAppId(), result.getPrepayId(),
                        loadPrivateKey(config.getPrivateKey()));
                yield JsonUtil.toJsonString(info);
            }
        };
    }

    @Override
    public PayQueryResult query(PayOrderDO order, PayChannelConfigDO config) {
        WxPayService service = factory.get(config);
        WxPayOrderQueryV3Result r;
        try {
            r = service.queryOrderV3(order.getOrderNo(), null);
        } catch (WxPayException e) {
            throw new BusinessException(BizBaseCodeEnum.REQUEST_ERROR.value(), "微信查单失败:" + e.getMessage());
        }
        PayQueryResult result = new PayQueryResult();
        result.setChannelOrderNo(r.getTransactionId());
        result.setRawResponse(JsonUtil.toJsonString(r));
        result.setPaid("SUCCESS".equals(r.getTradeState()));
        if (r.getAmount() != null) {
            result.setAmount(toYuan(r.getAmount().getTotal()));
        }
        result.setPayAt(parseTime(r.getSuccessTime()));
        return result;
    }

    @Override
    public void close(PayOrderDO order, PayChannelConfigDO config) {
        WxPayService service = factory.get(config);
        try {
            service.closeOrderV3(order.getOrderNo());
        } catch (WxPayException e) {
            throw new BusinessException(BizBaseCodeEnum.REQUEST_ERROR.value(), "微信关单失败:" + e.getMessage());
        }
    }

    @Override
    public RefundResult refund(RefundOrderDO refund, PayChannelConfigDO config) {
        // 微信v3退款需原订单总额(amount.total)，退款单不含此字段，回查原单
        PayOrderDO order = payOrderService.getById(refund.getPayOrderId());
        if (order == null) {
            throw new BusinessException(BizBaseCodeEnum.REQUEST_ERROR.value(), "原支付订单不存在");
        }
        WxPayService service = factory.get(config);
        WxPayRefundV3Request req = new WxPayRefundV3Request();
        req.setOutTradeNo(order.getOrderNo());
        req.setOutRefundNo(refund.getRefundNo());
        req.setReason(refund.getReason());
        req.setNotifyUrl(config.getRefundNotifyUrl());
        req.setAmount(new WxPayRefundV3Request.Amount()
                .setRefund(toFen(refund.getRefundAmount()))
                .setTotal(toFen(order.getAmount())));
        WxPayRefundV3Result r;
        try {
            r = service.refundV3(req);
        } catch (WxPayException e) {
            throw new BusinessException(BizBaseCodeEnum.REQUEST_ERROR.value(), "微信退款失败:" + e.getMessage());
        }
        RefundResult result = new RefundResult();
        result.setChannelRefundNo(r.getRefundId());
        result.setRawResponse(JsonUtil.toJsonString(r));
        result.setStatus(mapRefundStatus(r.getStatus()));
        result.setRefundAt(parseTime(r.getSuccessTime()));
        if (result.getStatus() == RefundResultStatus.FAILED) {
            result.setErrorMsg("微信退款状态:" + r.getStatus());
        }
        return result;
    }

    @Override
    public RefundResult queryRefund(RefundOrderDO refund, PayChannelConfigDO config) {
        WxPayService service = factory.get(config);
        WxPayRefundQueryV3Result r;
        try {
            r = service.refundQueryV3(refund.getRefundNo());
        } catch (WxPayException e) {
            throw new BusinessException(BizBaseCodeEnum.REQUEST_ERROR.value(), "微信退款查询失败:" + e.getMessage());
        }
        RefundResult result = new RefundResult();
        result.setChannelRefundNo(r.getRefundId());
        result.setRawResponse(JsonUtil.toJsonString(r));
        result.setStatus(mapRefundStatus(r.getStatus()));
        result.setRefundAt(parseTime(r.getSuccessTime()));
        if (result.getStatus() == RefundResultStatus.FAILED) {
            result.setErrorMsg("微信退款状态:" + r.getStatus());
        }
        return result;
    }

    @Override
    public PayNotifyParseResult parsePayNotify(String body, Map<String, String> headers, PayChannelConfigDO config)
            throws Exception {
        WxPayService service = factory.get(config);
        WxPayNotifyV3Result notify = service.parseOrderNotifyV3Result(body, sigHeader(headers));
        WxPayNotifyV3Result.DecryptNotifyResult d = notify.getResult();
        PayNotifyParseResult r = new PayNotifyParseResult();
        r.setVerifySuccess(true);
        r.setOutTradeNo(d.getOutTradeNo());
        r.setChannelNo(d.getTransactionId());
        r.setPaid("SUCCESS".equals(d.getTradeState()));
        if (d.getAmount() != null) {
            r.setAmount(toYuan(d.getAmount().getTotal()));
        }
        r.setPayAt(parseTime(d.getSuccessTime()));
        r.setRawBody(body);
        return r;
    }

    @Override
    public RefundNotifyParseResult parseRefundNotify(String body, Map<String, String> headers, PayChannelConfigDO config)
            throws Exception {
        WxPayService service = factory.get(config);
        WxPayRefundNotifyV3Result notify = service.parseRefundNotifyV3Result(body, sigHeader(headers));
        WxPayRefundNotifyV3Result.DecryptNotifyResult d = notify.getResult();
        RefundNotifyParseResult r = new RefundNotifyParseResult();
        r.setVerifySuccess(true);
        r.setOutRefundNo(d.getOutRefundNo());
        r.setChannelNo(d.getRefundId());
        String refundStatus = d.getRefundStatus();
        r.setRefundSuccess("SUCCESS".equals(refundStatus));
        if (d.getAmount() != null) {
            r.setRefundAmount(toYuan(d.getAmount().getRefund()));
        }
        r.setRefundAt(parseTime(d.getSuccessTime()));
        if (!r.isRefundSuccess()) {
            r.setErrorMsg("微信退款状态:" + refundStatus);
        }
        r.setRawBody(body);
        return r;
    }

    /**
     * 构造微信v3验签头（回调入口与退款回调入口共用）
     */
    private SignatureHeader sigHeader(Map<String, String> headers) {
        return new SignatureHeader(
                header(headers, "Wechatpay-Timestamp"),
                header(headers, "Wechatpay-Nonce"),
                header(headers, "Wechatpay-Signature"),
                header(headers, "Wechatpay-Serial"));
    }

    @Override
    public String buildNotifyAck(boolean success) {
        // 微信v3回调应答JSON
        Map<String, String> ack = new LinkedHashMap<>();
        ack.put("code", success ? "SUCCESS" : "FAIL");
        ack.put("message", success ? "成功" : "失败");
        return JsonUtil.toJsonString(ack);
    }

    // -------------------- helpers --------------------

    private TradeTypeEnum mapTradeType(PayModeEnum mode) {
        return switch (mode) {
            case JSAPI -> TradeTypeEnum.JSAPI;
            case NATIVE -> TradeTypeEnum.NATIVE;
            case APP -> TradeTypeEnum.APP;
            case H5 -> TradeTypeEnum.H5;
        };
    }

    private RefundResultStatus mapRefundStatus(String status) {
        if ("SUCCESS".equals(status)) {
            return RefundResultStatus.SUCCESS;
        }
        if ("PROCESSING".equals(status)) {
            return RefundResultStatus.PROCESSING;
        }
        // ABNORMAL / CLOSED / 其它
        return RefundResultStatus.FAILED;
    }

    private PrivateKey loadPrivateKey(String pem) {
        try {
            return PemUtils.loadPrivateKey(
                    new ByteArrayInputStream(pem.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new BusinessException(BizBaseCodeEnum.REQUEST_ERROR.value(), "微信商户私钥加载失败:" + e.getMessage());
        }
    }

    /**
     * 元 -> 分
     */
    private int toFen(BigDecimal yuan) {
        return yuan.movePointRight(2).intValueExact();
    }

    /**
     * 分 -> 元
     */
    private BigDecimal toYuan(Integer fen) {
        return fen == null ? null : new BigDecimal(fen).movePointLeft(2);
    }

    private OffsetDateTime parseTime(String rfc3339) {
        if (rfc3339 == null || rfc3339.isEmpty()) {
            return null;
        }
        try {
            return OffsetDateTime.parse(rfc3339);
        } catch (Exception e) {
            return null;
        }
    }

    private String header(Map<String, String> headers, String name) {
        for (Map.Entry<String, String> e : headers.entrySet()) {
            if (name.equalsIgnoreCase(e.getKey())) {
                return e.getValue();
            }
        }
        return null;
    }

}
