package com.wzkris.usercenter.api.notification.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Schema(description = "通知信息")
public class NotificationInfoResponse {

    @Schema(description = "通知ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "已读未读")
    private Boolean read;

    @Schema(description = "创建时间")
    private OffsetDateTime createAt;

}

