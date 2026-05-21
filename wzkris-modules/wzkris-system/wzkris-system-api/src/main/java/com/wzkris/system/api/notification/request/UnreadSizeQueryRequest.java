package com.wzkris.system.api.notification.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "通知类型参数")
public class UnreadSizeQueryRequest {

    @Parameter(description = "通知类型")
    private String notificationType;

}
