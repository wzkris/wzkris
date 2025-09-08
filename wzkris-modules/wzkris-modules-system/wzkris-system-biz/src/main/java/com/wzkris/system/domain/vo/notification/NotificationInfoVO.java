package com.wzkris.system.domain.vo.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "通知信息")
public class NotificationInfoVO {

    @Schema(description = "通知ID")
    private Long notificationId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "已读未读")
    private String readState;

    @Schema(description = "创建时间")
    private Date createAt;

}
