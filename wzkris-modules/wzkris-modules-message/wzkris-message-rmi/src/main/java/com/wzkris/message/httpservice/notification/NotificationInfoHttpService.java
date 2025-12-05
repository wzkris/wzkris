package com.wzkris.message.httpservice.notification;

import com.wzkris.common.httpservice.annotation.HttpServiceClient;
import com.wzkris.common.httpservice.constants.ServiceIdConstant;
import com.wzkris.message.httpservice.notification.fallback.NotificationHttpServiceFallback;
import com.wzkris.message.httpservice.notification.req.NotificationReq;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : RPC -- 系统通知服务
 * @since : 2024/12/16 12:55
 */
@HttpServiceClient(
        serviceId = ServiceIdConstant.MESSAGE,
        fallbackFactory = NotificationHttpServiceFallback.class
)
@HttpExchange(url = "/feign-notification")
public interface NotificationInfoHttpService {

    /**
     * 发送通知
     */
    @PostExchange("/send-to-users")
    void send2Users(@RequestBody NotificationReq notificationReq);

}
