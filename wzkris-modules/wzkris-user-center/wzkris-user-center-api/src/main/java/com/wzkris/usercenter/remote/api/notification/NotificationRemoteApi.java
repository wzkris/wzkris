package com.wzkris.usercenter.remote.api.notification;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.notification.request.NotificationSaveRequest;

public interface NotificationRemoteApi {

    Result<Void> send2Users(NotificationSaveRequest request);

}

