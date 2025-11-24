package com.wzkris.message.httpservice.notification.req;

import com.wzkris.common.core.enums.AuthTypeEnum;
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
public class NotificationReq implements Serializable {

    /**
     * 接收者ID
     */
    private List<Long> receiverIds;

    /**
     * 接收者类型
     */
    private AuthTypeEnum authType;

    private String title;

    private String content;

}
