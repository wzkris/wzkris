package com.wzkris.usercenter.api.notification;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.notification.request.NotificationInfoPageRequest;
import com.wzkris.usercenter.api.notification.request.UnreadSizeQueryRequest;
import com.wzkris.usercenter.api.notification.response.NotificationInfoResponse;

public interface TenantNotificationInfoApi {

    Result<Page<NotificationInfoResponse>> queryPage(NotificationInfoPageRequest request);

    Result<Void> markRead(IdRequest request);

    Result<Integer> queryUnreadSize(UnreadSizeQueryRequest request);

}
