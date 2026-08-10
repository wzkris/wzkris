package com.wzkris.payment.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付网关可调参数
 *
 * @author wzkris
 */
@Data
@Component
@ConfigurationProperties(prefix = "payment")
public class PaymentProperties {

    /**
     * 支付订单号前缀
     */
    private String orderNoPrefix = "PO";

    /**
     * 退款单号前缀
     */
    private String refundNoPrefix = "RF";

    /**
     * 默认订单过期分钟数
     */
    private Integer defaultExpireMinutes = 30;

    /**
     * 业务方通知最大重试次数
     */
    private Integer notifyMaxRetry = 8;

    /**
     * 业务方通知初始重试间隔（秒）
     */
    private Long notifyRetryIntervalSeconds = 30L;

}
