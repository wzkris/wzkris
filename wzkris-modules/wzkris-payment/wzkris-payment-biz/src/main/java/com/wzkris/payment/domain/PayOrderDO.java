package com.wzkris.payment.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.pay.PayModeEnum;
import com.wzkris.payment.enums.pay.PayStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * 支付订单
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "pay_order")
public class PayOrderDO extends BaseEntity {

    @Schema(description = "业务可读订单号")
    private String orderNo;

    @Schema(description = "业务类型")
    private String bizType;

    @Schema(description = "业务方订单号")
    private String bizNo;

    @Schema(description = "支付渠道")
    private PayChannelEnum channel;

    @Schema(description = "成交时渠道配置ID快照")
    private Long configId;

    @Schema(description = "支付方式")
    private PayModeEnum payMode;

    @Schema(description = "订单标题")
    private String subject;

    @Schema(description = "支付金额(元)")
    private BigDecimal amount;

    @Schema(description = "币种")
    private String currency;

    @Schema(description = "已退款金额(含退款中,超额退款护栏)")
    private BigDecimal refundedAmount;

    @Schema(description = "订单状态")
    private PayStatusEnum status;

    @Schema(description = "支付者标识(微信open_id/支付宝buyer_id)")
    private String payerId;

    @Schema(description = "客户端IP")
    private String clientIp;

    @Schema(description = "过期时间")
    private OffsetDateTime expireAt;

    @Schema(description = "渠道侧交易号")
    private String channelOrderNo;

    @Schema(description = "支付成功时间")
    private OffsetDateTime payAt;

    @Schema(description = "失败原因")
    private String failReason;

    @Schema(description = "业务方通知地址")
    private String notifyUrl;

    public PayOrderDO(Long id) {
        this.setId(id);
    }

}
