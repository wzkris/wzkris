package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.usercenter.enums.notification.NotificationTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * 系统通知
 *
 * @author wzkris
 */
@Data
@TableName(schema = "biz", value = "notification_info")
public class NotificationInfoDO {

    @TableId
    private Long notificationId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "通知类型")
    private NotificationTypeEnum notificationType;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "创建者ID")
    private Long creatorId;

    @Schema(description = "创建时间")
    private OffsetDateTime createAt;

    public NotificationInfoDO() {
        this.createAt = OffsetDateTime.now();
    }

}
