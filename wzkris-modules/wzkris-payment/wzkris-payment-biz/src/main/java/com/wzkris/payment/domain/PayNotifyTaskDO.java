package com.wzkris.payment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.payment.enums.notify.NotifyTaskStatusEnum;
import com.wzkris.payment.enums.notify.NotifyTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * 业务方通知任务（网关 -> 业务方，带重试）
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "pay_notify_task")
public class PayNotifyTaskDO extends BaseEntity {

    @TableId
    private Long taskId;

    @Schema(description = "通知类型 PAY/REFUND")
    private NotifyTypeEnum notifyType;

    @Schema(description = "支付订单ID")
    private Long payOrderId;

    @Schema(description = "退款订单ID(REFUND类型时填)")
    private Long refundOrderId;

    @Schema(description = "业务类型")
    private String bizType;

    @Schema(description = "业务方通知地址")
    private String targetUrl;

    @Schema(description = "通知报文")
    private String payload;

    @Schema(description = "最近一次HTTP状态码")
    private Integer httpStatus;

    @Schema(description = "已重试次数")
    private Integer retryCount;

    @Schema(description = "最大重试次数")
    private Integer maxRetry;

    @Schema(description = "下次重试时间")
    private OffsetDateTime nextRetryAt;

    @Schema(description = "任务状态")
    private NotifyTaskStatusEnum status;

    @Schema(description = "错误信息")
    private String errorMsg;
}
