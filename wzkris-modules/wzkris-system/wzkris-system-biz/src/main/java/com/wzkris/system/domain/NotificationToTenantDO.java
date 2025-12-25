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
@TableName(schema = "biz", value = "notification_to_tenant")
public class NotificationToTenantDO {

    @Schema(description = "通知ID")
    private Long notificationId;

    @Schema(description = "租户成员ID")
    private Long memberId;

    @Schema(description = "是否已读")
    private Boolean read;

    public NotificationToTenantDO(Long notificationId, Long memberId) {
        this.notificationId = notificationId;
        this.memberId = memberId;
        this.read = false;
    }

}
