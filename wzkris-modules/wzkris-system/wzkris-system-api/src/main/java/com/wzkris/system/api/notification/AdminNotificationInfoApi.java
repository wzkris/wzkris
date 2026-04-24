package com.wzkris.system.api.notification;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.request.common.IdRequest;
import com.wzkris.system.request.message.UnreadSizeQueryRequest;
import com.wzkris.system.response.notification.NotificationInfoResponse;

public interface AdminNotificationInfoApi {

    Result<Page<NotificationInfoResponse>> queryPage(Boolean read, String notificationType);

    Result<Void> markRead(IdRequest request);

    Result<Integer> queryUnreadSize(UnreadSizeQueryRequest request);

}
