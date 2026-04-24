package com.wzkris.system.request.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "通知类型参数")
public class UnreadSizeQueryRequest {

    @Schema(description = "通知类型")
    private String notificationType;

}
