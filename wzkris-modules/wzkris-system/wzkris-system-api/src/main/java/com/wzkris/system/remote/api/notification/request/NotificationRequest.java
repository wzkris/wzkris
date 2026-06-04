package com.wzkris.system.remote.api.notification.request;

import com.wzkris.common.core.enums.AuthTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest implements Serializable {

    private List<Long> receiverIds;

    private AuthTypeEnum toAuthType;

    private String title;

    private String content;

}
