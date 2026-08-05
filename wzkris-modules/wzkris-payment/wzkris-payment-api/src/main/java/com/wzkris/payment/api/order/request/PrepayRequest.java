package com.wzkris.payment.api.order.request;

import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.pay.PayModeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 统一下单参数体
 *
 * @author wzkris
 */
@Data
@Schema(description = "统一下单参数体")
public class PrepayRequest {

    @NotBlank(message = "业务类型不能为空")
    @Schema(description = "业务类型（标识业务方，用于回调路由）")
    private String bizType;

    @NotBlank(message = "业务订单号不能为空")
    @Schema(description = "业务方订单号（与 bizType 组成幂等键）")
    private String bizNo;

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

    @Schema(description = "支付者标识（JSAPI 必传：微信 open_id / 支付宝 buyer_id）")
    private String payerId;

    @Schema(description = "客户端IP")
    private String clientIp;

    @Schema(description = "订单过期分钟数（默认30）")
    private Integer expireMinutes;

    @Schema(description = "业务方支付结果通知地址")
    private String notifyUrl;
}
