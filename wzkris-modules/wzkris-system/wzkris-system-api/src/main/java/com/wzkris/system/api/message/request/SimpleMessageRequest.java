package com.wzkris.system.api.message.request;

import com.wzkris.system.enums.notification.NotificationTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "简易消息体")
public class SimpleMessageRequest {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "消息类型")
    private NotificationTypeEnum type;

    @Schema(description = "前端展示时间, 毫秒")
    private int duration;

    @Schema(description = "内容")
    private String content;

    public SimpleMessageRequest(String title, NotificationTypeEnum type, String content) {
        this.title = title;
        this.type = type;
        this.duration = 1500;
        this.content = content;
    }

}
