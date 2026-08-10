package com.wzkris.payment.remote.api.order.request;

import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.pay.PayModeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

/**
 * 生成订单并支付参数体
 *
 * <p>一次调用完成：登记订单意图生成 order_no -> 渠道预下单，返回渠道侧支付参数。
 * 不防重：重试多建的 PENDING 由过期关单 Job 兜底，预下单不扣款，不会重复扣款。
 *
 * @author wzkris
 */
@Data
@Schema(description = "生成订单并支付参数体")
public class PayOrderCreateRequest {

    @NotNull(message = "支付渠道不能为空")
    @Schema(description = "支付渠道")
    private PayChannelEnum channel;

    @NotNull(message = "渠道配置ID不能为空")
    @Schema(description = "渠道配置ID（由调用方决定使用的商户配置）")
    private Long configId;

    @NotNull(message = "支付方式不能为空")
    @Schema(description = "支付方式")
    private PayModeEnum payMode;

    @NotNull(message = "支付金额不能为空")
    @DecimalMin(value = "0.01", message = "支付金额必须大于0")
    @Schema(description = "支付金额（元）")
    private BigDecimal amount;

    @NotBlank(message = "订单标题不能为空")
    @Schema(description = "订单标题")
    private String subject;

    @URL(message = "支付结果通知地址不能为空")
    @Schema(description = "业务方支付结果通知地址")
    private String notifyUrl;

    @Schema(description = "支付者标识（JSAPI 必传：微信 open_id / 支付宝 buyer_id）")
    private String payerId;

    @Schema(description = "客户端IP")
    private String clientIp;

    @Schema(description = "订单过期分钟数（默认30）")
    private Integer expireMinutes;

}
