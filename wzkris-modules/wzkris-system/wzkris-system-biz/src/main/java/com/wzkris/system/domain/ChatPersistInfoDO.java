package com.wzkris.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.system.enums.chat.MediaFormatEnum;
import com.wzkris.system.enums.chat.ResourceTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@TableName(schema = "biz", value = "chat_persist_info")
public class ChatPersistInfoDO {

    @TableId
    private Long chatId;

    @Schema(description = "接收者ID")
    private Long receiverId;

    @Schema(description = "发送者ID")
    private Long senderId;

    @Schema(description = "发送时间")
    private OffsetDateTime sendTime;

    @Schema(description = "接收时间")
    private OffsetDateTime receiveTime;

    @Schema(description = "是否已读")
    private Boolean read;

    @Schema(description = "资源类型")
    private ResourceTypeEnum resourceType;

    @Schema(description = "二进制内容")
    private byte[] content;

    @Schema(description = "媒体格式")
    private MediaFormatEnum mediaFormat;

}
