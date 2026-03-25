package com.wzkris.usercenter.remote.notification;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.remote.annotation.RemoteInterface;
import com.wzkris.common.remote.constants.ServiceContextPathConstant;
import com.wzkris.common.remote.constants.ServiceIdConstant;
import com.wzkris.usercenter.remote.notification.req.NotificationReq;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : RPC -- 系统通知服务
 * @since : 2024/12/16 12:55
 */
@RemoteInterface(
        serviceId = ServiceIdConstant.SYSTEM,
        path = ServiceContextPathConstant.SYSTEM
)
@HttpExchange(url = "/notification-info-client")
public interface INotificationInfoRemote {

    /**
     * 发送通知
     */
    @PostExchange("/send-to-users")
    Result<Void> send2Users(@RequestBody NotificationReq notificationReq);

}
