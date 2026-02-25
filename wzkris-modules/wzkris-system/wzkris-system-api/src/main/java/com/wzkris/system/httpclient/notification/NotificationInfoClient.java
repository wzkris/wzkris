package com.wzkris.system.httpclient.notification;

import com.wzkris.common.httpclient.annotation.HttpClient;
import com.wzkris.common.httpclient.constants.ServiceContextPathConstant;
import com.wzkris.common.httpclient.constants.ServiceIdConstant;
import com.wzkris.system.httpclient.notification.fallback.NotificationInfoClientFallback;
import com.wzkris.system.httpclient.notification.req.NotificationReq;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : RPC -- 系统通知服务
 * @since : 2024/12/16 12:55
 */
@HttpClient(
        serviceId = ServiceIdConstant.SYSTEM,
        path = ServiceContextPathConstant.SYSTEM,
        fallbackFactory = NotificationInfoClientFallback.class
)
@HttpExchange(url = "/notification-info-client")
public interface NotificationInfoClient {

    /**
     * 发送通知
     */
    @PostExchange("/send-to-users")
    void send2Users(@RequestBody NotificationReq notificationReq);

}
