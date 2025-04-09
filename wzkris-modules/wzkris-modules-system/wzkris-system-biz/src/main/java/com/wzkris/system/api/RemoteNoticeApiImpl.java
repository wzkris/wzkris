package com.wzkris.system.api;

import com.wzkris.system.api.domain.request.SendNoticeReq;
import com.wzkris.system.constant.MessageConstants;
import com.wzkris.system.domain.dto.SimpleMessageDTO;
import com.wzkris.system.service.SysNoticeService;
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
public class RemoteNoticeApiImpl implements RemoteNoticeApi {

    private final SysNoticeService noticeService;

    @Override
    public void sendNotice(SendNoticeReq req) {
        noticeService.sendUsers(req.getUserIds(), new SimpleMessageDTO(req.getTitle(), MessageConstants.NOTICE_TYPE_SYSTEM, req.getContent()));
    }

}
