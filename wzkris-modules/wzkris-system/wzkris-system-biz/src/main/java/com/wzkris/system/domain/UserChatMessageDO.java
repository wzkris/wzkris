package com.wzkris.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@TableName(schema = "biz", value = "user_chat_message")
public class UserChatMessageDO {

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

    @Schema(description = "text/image/video/file")
    private String messageType;

    @Schema(description = "统一的内容字段，存储文本或二进制数据")
    private byte[] content;

    @Schema(description = "媒体格式(png/jpg/mp4/txt等)")
    private String mediaFormat;

}
