package com.wzkris.system.rmi;

import com.wzkris.system.rmi.domain.req.SendNoticeReq;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : RPC -- 通知服务
 * @since : 2024/12/16 12:55
 */
public interface RmiNoticeService {

    /**
     * 发送通知
     */
    void sendNotice(SendNoticeReq sendNoticeReq);
}
