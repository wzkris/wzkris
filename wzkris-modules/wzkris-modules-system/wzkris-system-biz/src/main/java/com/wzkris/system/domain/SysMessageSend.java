package com.wzkris.system.domain;

import com.wzkris.system.constant.MessageConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统消息发送表
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class SysMessageSend {

    @Schema(description = "接收用户ID")
    private Long userId;

    @Schema(description = "通知ID")
    private Long msgId;

    @Schema(description = "发送时间")
    private Long sendTime;

    @Schema(description = "已读1 未读0")
    private String readState;

    public SysMessageSend(Long userId, Long msgId) {
        this.userId = userId;
        this.msgId = msgId;
        this.readState = MessageConstants.NOTIFY_UNREAD;
    }
}
