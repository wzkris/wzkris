package com.wzkris.payment.provider.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.v3.util.AesUtils;
import com.github.binarywang.wxpay.v3.util.PemUtils;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.payment.domain.PayChannelConfigDO;
import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.domain.RefundOrderDO;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.pay.PayModeEnum;
import com.wzkris.payment.enums.refund.RefundStatusEnum;
import com.wzkris.payment.exception.PaymentConfigException;
import com.wzkris.payment.provider.PayChannelProvider;
import com.wzkris.payment.provider.model.*;
import lombok.extern.slf4j.Slf4j;
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
 * <p>退款为受理制：refund() 返回 REFUNDING 不立即置 SUCCESS，由异步退款回调终结。
 * 主动渠道调用以 {@link ChannelResult} 返回,可预期的渠道/配置失败走返回值不抛异常。
 *
 * @author wzkris
 */
@Slf4j
@Component
public class WxpayChannelProvider implements PayChannelProvider {

    @Override
    public PayChannelEnum channel() {
        return PayChannelEnum.WXPAY;
    }

    @Override
    public ChannelResult<PrepayResult> prepay(PayOrderDO order, PaymentProviderContext ctx) {
        PayChannelConfigDO config = ctx.config();
        WxPayUnifiedOrderV3Request req = new WxPayUnifiedOrderV3Request();
        req.setOutTradeNo(order.getOrderNo());
        req.setDescription(order.getSubject());
        req.setNotifyUrl(config.getNotifyUrl());
        if (order.getExpireAt() != null) {
            req.setTimeExpire(order.getExpireAt().toString());
        }
        req.setAmount(new WxPayUnifiedOrderV3Request.Amount().setTotal(toFen(order.getAmount())));
        if (order.getPayMode() == PayModeEnum.JSAPI) {
            req.setPayer(new WxPayUnifiedOrderV3Request.Payer().setOpenid(order.getPayerId()));
        }
        TradeTypeEnum tradeType = mapTradeType(order.getPayMode());
        try {
            WxPayService service = wxPayService(config);
            WxPayUnifiedOrderV3Result result = service.unifiedOrderV3(tradeType, req);
            String payload = buildPrepayPayload(tradeType, config, result);
            return ChannelResult.ok(new PrepayResult(payload));
        } catch (WxPayException e) {
            return ChannelResult.fail("微信预下单失败:" + e.getMessage());
        } catch (PaymentConfigException e) {
            return ChannelResult.fail(e.getMessage());
        }
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
    public ChannelResult<PayQueryResult> query(PayOrderDO order, PaymentProviderContext ctx) {
        PayChannelConfigDO config = ctx.config();
        try {
            WxPayService service = wxPayService(config);
            WxPayOrderQueryV3Result r = service.queryOrderV3(order.getOrderNo(), null);
            PayQueryResult result = new PayQueryResult();
            result.setChannelOrderNo(r.getTransactionId());
            result.setRawResponse(JsonUtil.toJsonString(r));
            result.setPaid("SUCCESS".equals(r.getTradeState()));
            if (r.getAmount() != null) {
                result.setAmount(toYuan(r.getAmount().getTotal()));
            }
            result.setPayAt(parseTime(r.getSuccessTime()));
            return ChannelResult.ok(result);
        } catch (WxPayException e) {
            return ChannelResult.fail("微信查单失败:" + e.getMessage());
        } catch (PaymentConfigException e) {
            return ChannelResult.fail(e.getMessage());
        }
    }

    @Override
    public ChannelResult<Void> close(PayOrderDO order, PaymentProviderContext ctx) {
        PayChannelConfigDO config = ctx.config();
        try {
            WxPayService service = wxPayService(config);
            service.closeOrderV3(order.getOrderNo());
            return ChannelResult.ok(null);
        } catch (WxPayException e) {
            return ChannelResult.fail("微信关单失败:" + e.getMessage());
        } catch (PaymentConfigException e) {
            return ChannelResult.fail(e.getMessage());
        }
    }

    @Override
    public ChannelResult<RefundResult> refund(RefundOrderDO refund, PayOrderDO order, PaymentProviderContext ctx) {
        PayChannelConfigDO config = ctx.config();
        WxPayRefundV3Request req = new WxPayRefundV3Request();
        req.setOutTradeNo(order.getOrderNo());
        req.setOutRefundNo(refund.getRefundNo());
        req.setReason(refund.getReason());
        req.setNotifyUrl(config.getRefundNotifyUrl());
        req.setAmount(new WxPayRefundV3Request.Amount()
                .setRefund(toFen(refund.getRefundAmount()))
                .setTotal(toFen(order.getAmount())));
        try {
            WxPayService service = wxPayService(config);
            WxPayRefundV3Result r = service.refundV3(req);
            RefundResult result = new RefundResult();
            result.setChannelRefundNo(r.getRefundId());
            result.setRawResponse(JsonUtil.toJsonString(r));
            result.setStatus(mapRefundStatus(r.getStatus()));
            result.setRefundAt(parseTime(r.getSuccessTime()));
            if (result.getStatus() == RefundStatusEnum.FAILED) {
                result.setErrorMsg("微信退款状态:" + r.getStatus());
            }
            return ChannelResult.ok(result);
        } catch (WxPayException e) {
            return ChannelResult.fail("微信退款失败:" + e.getMessage());
        } catch (PaymentConfigException e) {
            return ChannelResult.fail(e.getMessage());
        }
    }

    @Override
    public ChannelResult<RefundResult> queryRefund(RefundOrderDO refund, PaymentProviderContext ctx) {
        PayChannelConfigDO config = ctx.config();
        try {
            WxPayService service = wxPayService(config);
            WxPayRefundQueryV3Result r = service.refundQueryV3(refund.getRefundNo());
            RefundResult result = new RefundResult();
            result.setChannelRefundNo(r.getRefundId());
            result.setRawResponse(JsonUtil.toJsonString(r));
            result.setStatus(mapRefundStatus(r.getStatus()));
            result.setRefundAt(parseTime(r.getSuccessTime()));
            if (result.getStatus() == RefundStatusEnum.FAILED) {
                result.setErrorMsg("微信退款状态:" + r.getStatus());
            }
            return ChannelResult.ok(result);
        } catch (WxPayException e) {
            return ChannelResult.fail("微信退款查询失败:" + e.getMessage());
        } catch (PaymentConfigException e) {
            return ChannelResult.fail(e.getMessage());
        }
    }

    @Override
    public boolean verifyNotify(String body, Map<String, String> headers, PaymentProviderContext ctx) {
        try {
            WxPayService service = wxPayService(ctx.config());
            SignatureHeader h = sigHeader(headers);
            String message = h.getTimeStamp() + "\n" + h.getNonce() + "\n" + body + "\n";
            return service.getConfig().getVerifier()
                    .verify(h.getSerial(), message.getBytes(StandardCharsets.UTF_8), h.getSignature());
        } catch (Exception e) {
            log.error("微信异步通知验签异常", e);
            return false;
        }
    }

    @Override
    public PayNotifyResult parsePayNotify(String body, PaymentProviderContext ctx) {
        PayNotifyResult r = new PayNotifyResult();
        ObjectNode data;
        try {
            data = decryptResource(resourceOf(body), ctx.config().getApiKey());
        } catch (Exception e) {
            log.error("微信异步通知报文解析异常", e);
            r.setErrorMsg("微信回调报文解析异常");
            return r;
        }
        r.setRawBody(body);
        r.setOutTradeNo(data.path("out_trade_no").asText());
        r.setChannelNo(data.path("transaction_id").asText());
        r.setPaid("SUCCESS".equals(data.path("trade_state").asText()));
        if (data.has("amount")) {
            r.setAmount(toYuan(data.path("amount").path("total").asInt()));
        }
        r.setPayAt(parseTime(data.path("success_time").asText()));
        return r;
    }

    @Override
    public RefundNotifyResult parseRefundNotify(String body, PaymentProviderContext ctx) {
        RefundNotifyResult r = new RefundNotifyResult();
        ObjectNode data;
        try {
            data = decryptResource(resourceOf(body), ctx.config().getApiKey());
        } catch (Exception e) {
            log.error("微信异步通知报文解析异常", e);
            r.setErrorMsg("微信回调报文解析异常");
            return r;
        }
        r.setRawBody(body);
        r.setOutRefundNo(data.path("out_refund_no").asText());
        r.setChannelNo(data.path("refund_id").asText());
        String refundStatus = data.path("refund_status").asText();
        r.setRefundSuccess("SUCCESS".equals(refundStatus));
        if (data.has("amount")) {
            r.setRefundAmount(toYuan(data.path("amount").path("refund").asInt()));
        }
        r.setRefundAt(parseTime(data.path("success_time").asText()));
        if (!r.isRefundSuccess()) {
            r.setErrorMsg("微信退款状态:" + refundStatus);
        }
        return r;
    }

    /**
     * 解析通知 resource 节点（内含 AES-GCM 密文）
     */
    private ObjectNode resourceOf(String body) {
        return (ObjectNode) JsonUtil.readTree(body).get("resource");
    }

    /**
     * 用 APIv3 密钥解密 resource 密文为明文业务字段（验签已由 {@link #verifyNotify} 前置）。
     */
    private ObjectNode decryptResource(ObjectNode resource, String apiV3Key) throws Exception {
        String plain = AesUtils.decryptToString(
                resource.path("associated_data").asText(),
                resource.path("nonce").asText(),
                resource.path("ciphertext").asText(),
                apiV3Key);
        return JsonUtil.readTree(plain);
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

    /**
     * 无状态按 config 现建客户端：apiKey 存 APIv3 密钥、privateKey 存商户私钥 PEM、certSerialNo 商户证书序列号。
     * v3 平台证书由 apiV3Key 懒加载自动下载/轮转（getVerifier 触发 initApiV3HttpClient）。
     */
    private WxPayService wxPayService(PayChannelConfigDO config) {
        if (config.getAppId() == null
                || config.getMchId() == null
                || config.getApiKey() == null
                || config.getPrivateKey() == null
                || config.getCertSerialNo() == null) {
            throw new PaymentConfigException("微信支付配置不完整(appId/mchId/apiKey/privateKey/certSerialNo)");
        }
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(config.getAppId());
        payConfig.setMchId(config.getMchId());
        if (config.getSubAppId() != null) {
            payConfig.setSubAppId(config.getSubAppId());
        }
        if (config.getSubMchId() != null) {
            payConfig.setSubMchId(config.getSubMchId());
        }
        payConfig.setApiV3Key(config.getApiKey());
        payConfig.setPrivateKeyString(config.getPrivateKey());
        payConfig.setCertSerialNo(config.getCertSerialNo());
        WxPayService service = new WxPayServiceImpl();
        service.setConfig(payConfig);
        return service;
    }

    private TradeTypeEnum mapTradeType(PayModeEnum mode) {
        return switch (mode) {
            case JSAPI -> TradeTypeEnum.JSAPI;
            case NATIVE -> TradeTypeEnum.NATIVE;
            case APP -> TradeTypeEnum.APP;
            case H5 -> TradeTypeEnum.H5;
        };
    }

    private RefundStatusEnum mapRefundStatus(String status) {
        if ("SUCCESS".equals(status)) {
            return RefundStatusEnum.SUCCESS;
        }
        if ("PROCESSING".equals(status)) {
            return RefundStatusEnum.REFUNDING;
        }
        // ABNORMAL / CLOSED / 其它
        return RefundStatusEnum.FAILED;
    }

    private PrivateKey loadPrivateKey(String pem) {
        try {
            return PemUtils.loadPrivateKey(
                    new ByteArrayInputStream(pem.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new PaymentConfigException("微信商户私钥加载失败:" + e.getMessage(), e);
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
