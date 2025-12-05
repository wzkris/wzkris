package com.wzkris.message.httpservice.notification.fallback;

import com.wzkris.common.httpservice.fallback.HttpServiceFallback;
import com.wzkris.message.httpservice.notification.NotificationInfoHttpService;
import com.wzkris.message.httpservice.notification.req.NotificationReq;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotificationHttpServiceFallback implements HttpServiceFallback<NotificationInfoHttpService> {

    @Override
    public NotificationInfoHttpService create(Throwable cause) {
        return new NotificationInfoHttpService() {
            @Override
            public void send2Users(NotificationReq notificationReq) {
                log.error("send2Users => req: {}", notificationReq, cause);
            }
        };
    }

}
