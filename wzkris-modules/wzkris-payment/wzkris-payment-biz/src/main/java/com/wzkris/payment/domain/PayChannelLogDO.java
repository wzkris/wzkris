package com.wzkris.payment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.pay.PayModeEnum;
import com.wzkris.payment.enums.pay.PayStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 渠道交互留痕（一次下单/查单的请求响应留痕）
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "pay_channel_log")
public class PayChannelLogDO extends BaseEntity {

    @TableId
    private Long channelLogId;

    @Schema(description = "支付订单ID")
    private Long payOrderId;

    @Schema(description = "支付渠道")
    private PayChannelEnum channel;

    @Schema(description = "本次交互所用配置ID")
    private Long configId;

    @Schema(description = "渠道预下单号")
    private String channelPrepayNo;

    @Schema(description = "支付方式")
    private PayModeEnum payMode;

    @Schema(description = "渠道请求参数")
    private String requestParams;

    @Schema(description = "渠道返回参数")
    private String responseParams;

    @Schema(description = "交互状态")
    private PayStatusEnum status;
}
