package com.wzkris.system.api.notification;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.response.notification.NotificationInfoResponse;

public interface TenantNotificationInfoApi {

    Result<Page<NotificationInfoResponse>> queryPage(Boolean read, String notificationType);

    Result<Void> markRead(Long notificationId);

    Result<Integer> unreadSize(String notificationType);

}
