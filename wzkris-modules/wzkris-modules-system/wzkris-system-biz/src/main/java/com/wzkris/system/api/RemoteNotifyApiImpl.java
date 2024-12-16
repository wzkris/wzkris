package com.wzkris.system.api;

import com.wzkris.common.openfeign.annotation.InnerAuth;
import com.wzkris.system.api.domain.request.SendNotifyReq;
import com.wzkris.system.constant.MessageConstants;
import com.wzkris.system.domain.SysNotify;
import com.wzkris.system.service.SysNotifyService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 通知RPC
 * @since : 2024/12/16 12:55
 */
@Hidden
@InnerAuth
@RestController
@RequiredArgsConstructor
public class RemoteNotifyApiImpl implements RemoteNotifyApi {
    private final SysNotifyService notifyService;

    @Override
    public void sendSystemNotify(SendNotifyReq sendNotifyReq) {
        this.sendNotify(MessageConstants.NOTIFY_TYPE_SYSTEM, sendNotifyReq);
    }

    @Override
    public void sendDeviceNotify(SendNotifyReq sendNotifyReq) {
        this.sendNotify(MessageConstants.NOTIFY_TYPE_DEVICE, sendNotifyReq);
    }

    private void sendNotify(String type, SendNotifyReq sendNotifyReq) {
        SysNotify notify = new SysNotify();
        notify.setNotifyType(type);
        notify.setTitle(sendNotifyReq.getTitle());
        notify.setContent(sendNotifyReq.getContent());
        notifyService.sendNotify(sendNotifyReq.getUserIds(), notify);
    }
}
