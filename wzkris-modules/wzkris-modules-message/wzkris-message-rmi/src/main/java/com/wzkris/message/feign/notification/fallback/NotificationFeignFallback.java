package com.wzkris.message.feign.notification.fallback;

import com.wzkris.message.feign.notification.NotificationInfoFeign;
import com.wzkris.message.feign.notification.req.NotificationReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class NotificationFeignFallback implements FallbackFactory<NotificationInfoFeign> {

    @Override
    public NotificationInfoFeign create(Throwable cause) {
        return new NotificationInfoFeign() {
            @Override
            public void send2Users(NotificationReq notificationReq) {
                log.error("send2Users => req: {}", notificationReq, cause);
            }
        };
    }

}
