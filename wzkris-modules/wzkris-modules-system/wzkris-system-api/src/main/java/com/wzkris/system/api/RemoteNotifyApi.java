package com.wzkris.system.api;

import com.wzkris.common.core.constant.ApplicationNameConstants;
import com.wzkris.system.api.domain.request.SendNotifyReq;
import com.wzkris.system.api.fallback.RemoteNotifyApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.wzkris.common.core.constant.SecurityConstants.INNER_NOAUTH_REQUEST_PATH;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : RPC -- 通知服务
 * @since : 2024/12/16 12:55
 */
@FeignClient(value = ApplicationNameConstants.SYSTEM, contextId = "RemoteNotifyApi", fallbackFactory = RemoteNotifyApiFallback.class)
public interface RemoteNotifyApi {

    /**
     * 发送系统通知
     */
    @PostMapping(INNER_NOAUTH_REQUEST_PATH + "/send_system_notify")
    void sendSystemNotify(@RequestBody SendNotifyReq sendNotifyReq);

    /**
     * 发送设备通知
     */
    @PostMapping(INNER_NOAUTH_REQUEST_PATH + "/send_device_notify")
    void sendDeviceNotify(@RequestBody SendNotifyReq sendNotifyReq);
}
