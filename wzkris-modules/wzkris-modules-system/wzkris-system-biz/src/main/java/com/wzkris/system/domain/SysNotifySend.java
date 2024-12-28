package com.wzkris.system.domain;

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
public class SysNotifySend {

    @Schema(description = "接收用户ID")
    private Long userId;

    @Schema(description = "通知ID")
    private Long notifyId;

    @Schema(description = "发送时间")
    private Long sendTime;

    @Schema(description = "已读1 未读0")
    private String readState;

    public SysNotifySend(Long userId, Long notifyId) {
        this.userId = userId;
        this.notifyId = notifyId;
        this.readState = MessageConstants.NOTIFY_UNREAD;
    }
}
