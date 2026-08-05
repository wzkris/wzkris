package com.wzkris.payment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.notify.NotifyTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * 渠道回调记录（渠道->网关，验签+幂等）
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "pay_channel_notify")
public class PayChannelNotifyDO extends BaseEntity {

    @TableId
    private Long notifyId;

    @Schema(description = "支付渠道")
    private PayChannelEnum channel;

    @Schema(description = "回调类型 PAY/REFUND(验签失败时可为空)")
    private NotifyTypeEnum notifyType;

    @Schema(description = "我方业务号(PAY=order_no/REFUND=refund_no),幂等键(验签失败时可为空)")
    private String outBusinessNo;

    @Schema(description = "渠道侧号(transaction_id/refund_id/trade_no),仅存档")
    private String channelNo;

    @Schema(description = "回调原始报文")
    private String notifyData;

    @Schema(description = "验签结果")
    private Boolean verifyResult;

    @Schema(description = "是否已处理(幂等标记)")
    private Boolean processed;

    @Schema(description = "处理时间")
    private OffsetDateTime processedAt;

    @Schema(description = "处理错误信息")
    private String errorMsg;
}
