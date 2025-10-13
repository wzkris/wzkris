package com.wzkris.message.feign.notification;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.message.feign.notification.fallback.NotificationFeignFallback;
import com.wzkris.message.feign.notification.req.NotificationReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : RPC -- 系统通知服务
 * @since : 2024/12/16 12:55
 */
@FeignClient(name = ServiceIdConstant.SYSTEM, contextId = "NotificationInfoFeign",
        fallbackFactory = NotificationFeignFallback.class,
        path = "/feign-notification")
public interface NotificationInfoFeign {

    /**
     * 发送通知
     */
    @PostMapping("/send-to-users")
    void send2Users(@RequestBody NotificationReq notificationReq);

}
