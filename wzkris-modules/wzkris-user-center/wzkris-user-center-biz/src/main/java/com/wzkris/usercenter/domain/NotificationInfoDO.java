package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.usercenter.enums.notification.NotificationTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统通知
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(schema = "biz", value = "notification_info")
public class NotificationInfoDO extends BaseEntity {

    @TableId
    private Long notificationId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "通知类型")
    private NotificationTypeEnum notificationType;

    @Schema(description = "内容")
    private String content;

}
