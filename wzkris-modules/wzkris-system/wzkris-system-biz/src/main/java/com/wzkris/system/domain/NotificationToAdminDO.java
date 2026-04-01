package com.wzkris.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通知发送表
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "notification_to_admin")
public class NotificationToAdminDO {

    @Schema(description = "通知ID")
    private Long notificationId;

    @Schema(description = "管理员ID")
    private Long adminId;

    @Schema(description = "是否已读")
    private Boolean read;

    public NotificationToAdminDO(Long notificationId, Long adminId) {
        this.notificationId = notificationId;
        this.adminId = adminId;
        this.read = false;
    }

}
