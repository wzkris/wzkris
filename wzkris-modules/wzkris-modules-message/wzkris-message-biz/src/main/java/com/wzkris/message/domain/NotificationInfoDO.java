package com.wzkris.message.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

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

    @Schema(description = "通知类型（0系统通知 1设备告警）")
    private String notificationType;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "创建者ID")
    private Long creatorId;

    @Schema(description = "创建时间")
    private Date createAt;

    public NotificationInfoDO() {
        this.createAt = new Date();
    }

}
