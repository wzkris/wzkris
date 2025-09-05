package com.wzkris.system.rmi.fallback;

import com.wzkris.system.rmi.NotificationInfoFeign;
import com.wzkris.system.rmi.domain.req.NotificationReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
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
