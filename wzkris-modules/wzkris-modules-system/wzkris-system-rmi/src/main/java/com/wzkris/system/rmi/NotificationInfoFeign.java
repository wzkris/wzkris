package com.wzkris.system.rmi;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.common.openfeign.core.RmiFeign;
import com.wzkris.system.rmi.domain.req.NotificationReq;
import com.wzkris.system.rmi.fallback.NotificationFeignFallback;
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
public interface NotificationInfoFeign extends RmiFeign {

    /**
     * 发送通知
     */
    @PostMapping("/send-to-users")
    void send2Users(@RequestBody NotificationReq notificationReq);

}
