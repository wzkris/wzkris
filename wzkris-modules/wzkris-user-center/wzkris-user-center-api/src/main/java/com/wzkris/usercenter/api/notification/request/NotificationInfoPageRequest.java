package com.wzkris.usercenter.api.notification.request;

import com.wzkris.common.orm.request.PagingRequest;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "通知分页查询")
public class NotificationInfoPageRequest extends PagingRequest {

    @Parameter(description = "是否已读")
    private Boolean read;

    @Parameter(description = "通知类型")
    private String notificationType;

}
