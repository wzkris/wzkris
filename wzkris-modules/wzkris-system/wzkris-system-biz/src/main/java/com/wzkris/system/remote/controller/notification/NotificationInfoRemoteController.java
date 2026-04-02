package com.wzkris.system.remote.controller.notification;

import com.wzkris.common.core.model.Result;
import com.wzkris.system.remote.api.notification.NotificationInfoRemoteApi;
import com.wzkris.system.remote.api.notification.request.NotificationRequest;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/notification-info-remote")
@RequiredArgsConstructor
public class NotificationInfoRemoteController {

    private final NotificationInfoRemoteApi notificationInfoRemoteApi;

    /**
     * 发送通知
     */
    @PostMapping("/send-to-users")
    public Result<Void> send2Users(@RequestBody NotificationRequest request) {
        return notificationInfoRemoteApi.send2Users(request);
    }

}





