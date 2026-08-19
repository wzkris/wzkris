// com.wzkris.payment.provider.impl.AlipayPaymentProvider.java
package com.wzkris.payment.provider.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.payment.domain.PayChannelConfigDO;
import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.domain.RefundOrderDO;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.refund.RefundStatusEnum;
import com.wzkris.payment.exception.PaymentConfigException;
import com.wzkris.payment.provider.PayChannelProvider;
import com.wzkris.payment.provider.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 支付宝策略（alipay-sdk-java 3.1.0）
 *
 * <p>配置映射：appId=应用ID、privateKey=应用私钥(签名请求)、publicCert=支付宝公钥(验签回调)。
 * 无状态按 config 现建 {@link AlipayClient}（同微信 provider 内联），配置变更随 DB 即时生效。
 *
 * <p>payMode 映射：NATIVE→当面付(返回二维码)、H5→手机网站支付(返回自动提交表单 HTML)、
 * APP→APP支付(返回签名 orderStr)；JSAPI 支付宝无对应产品，由白名单校验在编排层拦截，此处兜底。
 *
 * <p>支付宝退款为同步受理制：refund() 同步返回即退款完成，置 SUCCESS（区别于微信的受理后异步）；
 * 异步退款回调(fund_change)为冗余确认路径，幂等模板已兼容。
 *
 * @author wzkris
 */
@Slf4j
@Component
public class AlipayChannelProvider implements PayChannelProvider {

    private static final String GATEWAY = "https://openapi.alipay.com/gateway.do";

    private static final String SIGN_TYPE = "RSA2";

    private static final DateTimeFormatter NOTIFY_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public PayChannelEnum channel() {
        return PayChannelEnum.ALIPAY;
    }

    @Override
    public ChannelResult<PrepayResult> prepay(PayOrderDO order, PaymentProviderContext ctx) {
        PayChannelConfigDO config = ctx.config();
        try {
            AlipayClient client = client(config);
            // 公共业务参数（各产品共用）
            Map<String, Object> biz = new LinkedHashMap<>();
            biz.put("out_trade_no", order.getOrderNo());
            biz.put("total_amount", toYuanStr(order.getAmount()));
            biz.put("subject", order.getSubject());
            if (order.getExpireAt() != null) {
                biz.put("timeout_express", expireMinutes(order.getExpireAt()) + "m");
            }
            return switch (order.getPayMode()) {
                case NATIVE -> {
                    // 当面付：返回二维码内容，前端扫码
                    AlipayTradePrecreateRequest req = new AlipayTradePrecreateRequest();
                    req.setNotifyUrl(config.getNotifyUrl());
                    req.setBizContent(JsonUtil.toJsonString(biz));
                    AlipayTradePrecreateResponse resp = client.execute(req);
                    if (!resp.isSuccess()) {
                        yield ChannelResult.fail("支付宝预下单失败:" + resp.getSubMsg());
                    }
                    yield ChannelResult.ok(new PrepayResult(resp.getQrCode()));
                }
                case H5 -> {
                    // 手机网站支付：返回提交表单 HTML
                    AlipayTradeWapPayRequest req = new AlipayTradeWapPayRequest();
                    req.setNotifyUrl(config.getNotifyUrl());
                    req.setBizContent(JsonUtil.toJsonString(biz));
                    AlipayTradeWapPayResponse resp = client.pageExecute(req);
                    yield ChannelResult.ok(new PrepayResult(resp.getBody()));
                }
                case APP -> {
                    // APP支付：返回签名后的唤起参数
                    AlipayTradeAppPayRequest req = new AlipayTradeAppPayRequest();
                    req.setNotifyUrl(config.getNotifyUrl());
                    req.setBizContent(JsonUtil.toJsonString(biz));
                    AlipayTradeAppPayResponse resp = client.sdkExecute(req);
                    yield ChannelResult.ok(new PrepayResult(resp.getBody()));
                }
                case JSAPI -> ChannelResult.fail("支付宝不支持JSAPI支付");
            };
        } catch (PaymentConfigException e) {
            return ChannelResult.fail(e.getMessage());
        } catch (AlipayApiException e) {
            return ChannelResult.fail("支付宝预下单失败:" + e.getErrMsg());
        }
    }

    @Override
    public ChannelResult<PayQueryResult> query(PayOrderDO order, PaymentProviderContext ctx) {
        PayChannelConfigDO config = ctx.config();
        try {
            Map<String, Object> biz = new LinkedHashMap<>();
            biz.put("out_trade_no", order.getOrderNo());
            AlipayTradeQueryRequest req = new AlipayTradeQueryRequest();
            req.setBizContent(JsonUtil.toJsonString(biz));
            AlipayTradeQueryResponse resp = client(config).execute(req);
            if (!resp.isSuccess()) {
                return ChannelResult.fail("支付宝查单失败:" + resp.getSubMsg());
            }
            PayQueryResult result = new PayQueryResult();
            result.setChannelOrderNo(resp.getTradeNo());
            result.setRawResponse(JsonUtil.toJsonString(resp));
            result.setPaid(isPaid(resp.getTradeStatus()));
            result.setAmount(parseYuan(resp.getTotalAmount()));
            result.setPayAt(toOffsetDateTime(resp.getSendPayDate()));
            return ChannelResult.ok(result);
        } catch (PaymentConfigException e) {
            return ChannelResult.fail(e.getMessage());
        } catch (AlipayApiException e) {
            return ChannelResult.fail("支付宝查单失败:" + e.getErrMsg());
        }
    }

    @Override
    public ChannelResult<Void> close(PayOrderDO order, PaymentProviderContext ctx) {
        PayChannelConfigDO config = ctx.config();
        try {
            Map<String, Object> biz = new LinkedHashMap<>();
            biz.put("out_trade_no", order.getOrderNo());
            AlipayTradeCloseRequest req = new AlipayTradeCloseRequest();
            req.setBizContent(JsonUtil.toJsonString(biz));
            AlipayTradeCloseResponse resp = client(config).execute(req);
            if (!resp.isSuccess()) {
                return ChannelResult.fail("支付宝关单失败:" + resp.getSubMsg());
            }
            return ChannelResult.ok(null);
        } catch (PaymentConfigException e) {
            return ChannelResult.fail(e.getMessage());
        } catch (AlipayApiException e) {
            return ChannelResult.fail("支付宝关单失败:" + e.getErrMsg());
        }
    }

    @Override
    public ChannelResult<RefundResult> refund(RefundOrderDO refund, PayOrderDO order, PaymentProviderContext ctx) {
        PayChannelConfigDO config = ctx.config();
        try {
            Map<String, Object> biz = new LinkedHashMap<>();
            biz.put("out_trade_no", order.getOrderNo());
            // out_request_no 为退款请求号(幂等)，异步 fund_change 通知以其回显
            biz.put("out_request_no", refund.getRefundNo());
            biz.put("refund_amount", toYuanStr(refund.getRefundAmount()));
            if (refund.getReason() != null) {
                biz.put("refund_reason", refund.getReason());
            }
            AlipayTradeRefundRequest req = new AlipayTradeRefundRequest();
            req.setNotifyUrl(config.getRefundNotifyUrl());
            req.setBizContent(JsonUtil.toJsonString(biz));
            AlipayTradeRefundResponse resp = client(config).execute(req);
            if (!resp.isSuccess()) {
                return ChannelResult.fail("支付宝退款失败:" + resp.getSubMsg());
            }
            // 同步受理制：返回成功即退款完成
            RefundResult result = new RefundResult();
            result.setChannelRefundNo(resp.getTradeNo());
            result.setRawResponse(JsonUtil.toJsonString(resp));
            result.setRefundAt(toOffsetDateTime(resp.getGmtRefundPay()));
            result.setStatus(RefundStatusEnum.SUCCESS);
            return ChannelResult.ok(result);
        } catch (PaymentConfigException e) {
            return ChannelResult.fail(e.getMessage());
        } catch (AlipayApiException e) {
            return ChannelResult.fail("支付宝退款失败:" + e.getErrMsg());
        }
    }

    @Override
    public ChannelResult<RefundResult> queryRefund(RefundOrderDO refund, PaymentProviderContext ctx) {
        PayChannelConfigDO config = ctx.config();
        try {
            Map<String, Object> biz = new LinkedHashMap<>();
            biz.put("out_trade_no", refund.getOrderNo());
            biz.put("out_request_no", refund.getRefundNo());
            AlipayTradeFastpayRefundQueryRequest req = new AlipayTradeFastpayRefundQueryRequest();
            req.setBizContent(JsonUtil.toJsonString(biz));
            AlipayTradeFastpayRefundQueryResponse resp = client(config).execute(req);
            if (!resp.isSuccess()) {
                return ChannelResult.fail("支付宝退款查询失败:" + resp.getSubMsg());
            }
            RefundResult result = new RefundResult();
            result.setChannelRefundNo(resp.getTradeNo());
            result.setRawResponse(JsonUtil.toJsonString(resp));
            result.setStatus(mapRefundStatus(resp.getRefundStatus()));
            return ChannelResult.ok(result);
        } catch (PaymentConfigException e) {
            return ChannelResult.fail(e.getMessage());
        } catch (AlipayApiException e) {
            return ChannelResult.fail("支付宝退款查询失败:" + e.getErrMsg());
        }
    }

    @Override
    public boolean verifyNotify(String body, Map<String, String> headers, PaymentProviderContext ctx) {
        try {
            Map<String, String> params = parseFormBody(body);
            return AlipaySignature.rsaCheckV1(params, ctx.config().getPublicCert(), "UTF-8", SIGN_TYPE);
        } catch (Exception e) {
            log.warn("支付宝异步通知验签异常", e);
            return false;
        }
    }

    @Override
    public PayNotifyResult parsePayNotify(String body, PaymentProviderContext ctx) {
        Map<String, String> params = parseFormBody(body);
        PayNotifyResult r = new PayNotifyResult();
        r.setRawBody(body);
        r.setOutTradeNo(params.get("out_trade_no"));
        r.setChannelNo(params.get("trade_no"));
        // 支付成功通知 trade_status 为 TRADE_SUCCESS / TRADE_FINISHED
        r.setPaid(isPaid(params.get("trade_status")));
        r.setAmount(parseYuan(params.get("total_amount")));
        r.setPayAt(parseNotifyTime(params.get("gmt_payment")));
        return r;
    }

    @Override
    public RefundNotifyResult parseRefundNotify(String body, PaymentProviderContext ctx) {
        // fund_change 通知：out_biz_no 回显我方退款号
        Map<String, String> params = parseFormBody(body);
        RefundNotifyResult r = new RefundNotifyResult();
        r.setRawBody(body);
        r.setOutRefundNo(params.get("out_biz_no"));
        r.setChannelNo(params.get("trade_no"));
        // fund_change 仅在资金实动后发出，即退款成功（同步退款已终结，此为冗余确认）
        r.setRefundSuccess(true);
        r.setRefundAmount(parseYuan(params.get("refund_fee")));
        r.setRefundAt(parseNotifyTime(params.get("notify_time")));
        return r;
    }

    @Override
    public String buildNotifyAck(boolean success) {
        // 支付宝要求回调应答固定 "success" / "fail"
        return success ? "success" : "fail";
    }

    // -------------------- helpers --------------------

    /**
     * 无状态按 config 现建客户端：publicCert 存支付宝公钥（验签回调），privateKey 存应用私钥（签名请求）。
     */
    private AlipayClient client(PayChannelConfigDO config) {
        if (config.getAppId() == null || config.getPrivateKey() == null || config.getPublicCert() == null) {
            throw new PaymentConfigException("支付宝配置不完整(appId/privateKey/publicCert)");
        }
        return DefaultAlipayClient.builder(GATEWAY, config.getAppId(), config.getPrivateKey())
                .alipayPublicKey(config.getPublicCert())
                .signType(SIGN_TYPE)
                .charset("UTF-8")
                .build();
    }

    /**
     * 表单POST报文解析为参数Map（支付宝异步通知为 x-www-form-urlencoded）
     */
    private Map<String, String> parseFormBody(String body) {
        Map<String, String> params = new LinkedHashMap<>();
        if (body == null || body.isEmpty()) {
            return params;
        }
        for (String pair : body.split("&")) {
            int idx = pair.indexOf('=');
            if (idx < 0) {
                continue;
            }
            params.put(urlDecode(pair.substring(0, idx)), urlDecode(pair.substring(idx + 1)));
        }
        return params;
    }

    private String urlDecode(String s) {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }

    private boolean isPaid(String tradeStatus) {
        return "TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus);
    }

    private RefundStatusEnum mapRefundStatus(String status) {
        if ("REFUND_SUCCESS".equals(status)) {
            return RefundStatusEnum.SUCCESS;
        }
        if ("REFUND_CLOSED".equals(status) || "REFUND_FAIL".equals(status)) {
            return RefundStatusEnum.FAILED;
        }
        return RefundStatusEnum.REFUNDING;
    }

    private long expireMinutes(OffsetDateTime expireAt) {
        return Math.max(1, Duration.between(OffsetDateTime.now(), expireAt).toMinutes());
    }

    /**
     * 元 -> 金额字符串（两位小数）
     */
    private String toYuanStr(BigDecimal yuan) {
        return yuan.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private BigDecimal parseYuan(String yuan) {
        return yuan == null || yuan.isEmpty() ? null : new BigDecimal(yuan);
    }

    /**
     * java.util.Date -> OffsetDateTime（取系统默认时区偏移，比对按瞬时值）
     */
    private OffsetDateTime toOffsetDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atOffset(ZoneId.systemDefault().getRules().getOffset(date.toInstant()));
    }

    /**
     * 通知时间 "yyyy-MM-dd HH:mm:ss" -> OffsetDateTime（支付宝服务端为 GMT+8）
     */
    private OffsetDateTime parseNotifyTime(String time) {
        if (time == null || time.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(time, NOTIFY_TIME).atOffset(ZoneOffset.ofHours(8));
        } catch (Exception e) {
            return null;
        }
    }

}
