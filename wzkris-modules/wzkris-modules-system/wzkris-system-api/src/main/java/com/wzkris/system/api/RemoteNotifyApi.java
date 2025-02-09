package com.wzkris.system.api;

import com.wzkris.system.api.domain.request.SendNotifyReq;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : RPC -- 通知服务
 * @since : 2024/12/16 12:55
 */
public interface RemoteNotifyApi {

    /**
     * 发送系统通知
     */
    void sendSystemNotify(SendNotifyReq sendNotifyReq);

    /**
     * 发送设备通知
     */
    void sendDeviceNotify(SendNotifyReq sendNotifyReq);
}
