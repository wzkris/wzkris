package com.wzkris.system.api;

import com.wzkris.common.openfeign.annotation.InnerAuth;
import com.wzkris.system.api.domain.request.SendNotifyReq;
import com.wzkris.system.constant.MessageConstants;
import com.wzkris.system.domain.dto.SimpleMessageDTO;
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
    public void sendSystemNotify(SendNotifyReq req) {
        notifyService.sendNotify(req.getUserIds(), new SimpleMessageDTO(req.getTitle(), MessageConstants.NOTIFY_TYPE_SYSTEM, req.getContent()));
    }

    @Override
    public void sendDeviceNotify(SendNotifyReq req) {
        notifyService.sendNotify(req.getUserIds(), new SimpleMessageDTO(req.getTitle(), MessageConstants.NOTIFY_TYPE_DEVICE, req.getContent()));
    }

}
