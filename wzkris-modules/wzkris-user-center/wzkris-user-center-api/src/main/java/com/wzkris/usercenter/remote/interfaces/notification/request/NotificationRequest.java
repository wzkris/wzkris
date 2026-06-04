package com.wzkris.usercenter.remote.interfaces.notification.request;

import com.wzkris.common.core.enums.AuthTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 发送通知请求体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest implements Serializable {

    @Schema(description = "接收者ID")
    private List<Long> receiverIds;

    @Schema(description = "接收者类型")
    private AuthTypeEnum toAuthType;

    private String title;

    private String content;

}

