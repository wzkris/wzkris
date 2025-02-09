package com.wzkris.system.api;

import com.wzkris.system.api.domain.request.SendNotifyReq;
import com.wzkris.system.constant.MessageConstants;
import com.wzkris.system.domain.dto.SimpleMessageDTO;
import com.wzkris.system.service.SysNotifyService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 通知RPC
 * @since : 2024/12/16 12:55
 */
@Service
@DubboService
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
