package com.wzkris.usercenter.remote.controller.notification;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.notification.NotificationRemoteApi;
import com.wzkris.usercenter.remote.api.notification.request.NotificationSaveRequest;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/notification-remote")
@RequiredArgsConstructor
public class NotificationRemoteController {

    private final NotificationRemoteApi notificationRemoteApi;

    @Schema(description = "发送通知")
    @PostMapping("/send-to-users")
    public Result<Void> send2Users(@RequestBody NotificationSaveRequest request) {
        return notificationRemoteApi.send2Users(request);
    }

}





