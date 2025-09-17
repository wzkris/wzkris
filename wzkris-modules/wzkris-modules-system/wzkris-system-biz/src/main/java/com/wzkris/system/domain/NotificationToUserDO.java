package com.wzkris.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统通知发送表
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "notification_to_user")
public class NotificationToUserDO {

    @Schema(description = "通知ID")
    private Long notificationId;

    @Schema(description = "接收用户ID")
    private Long userId;

    @Schema(description = "是否已读")
    private Boolean read;

    public NotificationToUserDO(Long notificationId, Long userId) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.read = false;
    }

}
