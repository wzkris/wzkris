package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 通知发送表
 *
 * @author wzkris
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(schema = "biz", value = "notification_to_admin")
public class NotificationToAdminDO extends BaseEntity {

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
