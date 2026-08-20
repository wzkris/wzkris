package com.wzkris.payment.exception;

/**
 * 支付渠道配置异常:配置字段缺失、商户私钥加载失败等配置级问题。
 *
 * <p>仅在 provider 内部抛出并由 {@code ChannelResult.fail} 捕获转换,不外泄到编排层。
 * 与 {@code BusinessException}(业务校验)区分:配置错误是运维/部署级,非业务校验失败。
 *
 * @author wzkris
 */
public class PaymentConfigException extends RuntimeException {

    public PaymentConfigException(String message) {
        super(message);
    }

    public PaymentConfigException(String message, Throwable cause) {
        super(message, cause);
    }

}
