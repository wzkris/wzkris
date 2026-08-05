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
@TableName(schema = "biz", value = "notification_to_tenant")
public class NotificationToTenantDO extends BaseEntity {

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
