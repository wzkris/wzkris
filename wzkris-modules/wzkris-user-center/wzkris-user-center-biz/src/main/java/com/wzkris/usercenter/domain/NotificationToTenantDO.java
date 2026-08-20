package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@TableName(schema = "biz", value = "notification_to_tenant")
public class NotificationToTenantDO extends BaseEntity {

    @Schema(description = "通知ID")
    private Long notificationId;

    @Schema(description = "租户用户ID")
    private Long tenantUserId;

    @Schema(description = "是否已读")
    private Boolean read;

    public NotificationToTenantDO(Long notificationId, Long tenantUserId) {
        this.notificationId = notificationId;
        this.tenantUserId = tenantUserId;
        this.read = false;
    }

}
