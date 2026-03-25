package com.wzkris.system.remote.api.notification;

import com.wzkris.common.core.model.Result;
import com.wzkris.system.remote.api.notification.request.NotificationRequest;

public interface NotificationInfoRemoteApi {

    Result<Void> send2Users(NotificationRequest request);

}

