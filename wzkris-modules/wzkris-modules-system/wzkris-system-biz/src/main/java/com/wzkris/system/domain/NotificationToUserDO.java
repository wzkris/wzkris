package com.wzkris.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.system.constant.MessageConstants;
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

    @Schema(description = "已读1 未读0")
    private String readState;

    public NotificationToUserDO(Long notificationId, Long userId) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.readState = MessageConstants.UNREAD;
    }

}
