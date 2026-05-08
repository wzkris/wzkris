package com.wzkris.system.request.message;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "通知分页查询")
public class NotificationInfoPageRequest {

    @Parameter(description = "是否已读")
    private Boolean read;

    @Parameter(description = "通知类型")
    private String notificationType;
}
