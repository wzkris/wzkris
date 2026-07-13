package com.wzkris.usercenter.remote.impl.notification;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.api.notification.request.SimpleMessageRequest;
import com.wzkris.usercenter.enums.notification.NotificationTypeEnum;
import com.wzkris.usercenter.remote.api.notification.NotificationInfoRemoteApi;
import com.wzkris.usercenter.remote.api.notification.request.NotificationRequest;
import com.wzkris.usercenter.service.NotificationInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NotificationInfoRemoteApiImpl implements NotificationInfoRemoteApi {

    private final NotificationInfoService notificationInfoService;

    @Override
    public Result<Void> send2Users(NotificationRequest request) {
        if (Objects.equals(request.getToAuthType(), AuthTypeEnum.ADMIN)) {
            notificationInfoService.save2Admin(
                    request.getReceiverIds(),
                    new SimpleMessageRequest(request.getTitle(), NotificationTypeEnum.SYSTEM, request.getContent()));
        } else if (Objects.equals(request.getToAuthType(), AuthTypeEnum.TENANT)) {
            notificationInfoService.save2Tenant(
                    request.getReceiverIds(),
                    new SimpleMessageRequest(request.getTitle(), NotificationTypeEnum.SYSTEM, request.getContent()));
        }
        return Result.ok();
    }

}

