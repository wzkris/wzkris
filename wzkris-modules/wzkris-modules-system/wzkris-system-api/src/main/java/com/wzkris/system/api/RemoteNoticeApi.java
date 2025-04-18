package com.wzkris.system.api;

import com.wzkris.system.api.domain.request.SendNoticeReq;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : RPC -- 通知服务
 * @since : 2024/12/16 12:55
 */
public interface RemoteNoticeApi {

    /**
     * 发送通知
     */
    void sendNotice(SendNoticeReq sendNoticeReq);

}
