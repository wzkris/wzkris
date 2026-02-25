package com.wzkris.system.httpclient.notification.fallback;

import com.wzkris.common.httpclient.fallback.HttpClientFallback;
import com.wzkris.system.httpclient.notification.NotificationInfoClient;
import com.wzkris.system.httpclient.notification.req.NotificationReq;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotificationInfoClientFallback implements HttpClientFallback<NotificationInfoClient> {

    @Override
    public NotificationInfoClient create(Throwable cause) {
        return new NotificationInfoClient() {
            @Override
            public void send2Users(NotificationReq notificationReq) {
                log.error("send2Users => req: {}", notificationReq, cause);
            }
        };
    }

}
