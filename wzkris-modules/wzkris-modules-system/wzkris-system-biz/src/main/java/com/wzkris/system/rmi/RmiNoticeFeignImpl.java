package com.wzkris.system.rmi;

import com.wzkris.system.constant.MessageConstants;
import com.wzkris.system.domain.dto.SimpleMessageDTO;
import com.wzkris.system.rmi.domain.req.SendNoticeReq;
import com.wzkris.system.service.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 通知RPC
 * @since : 2024/12/16 12:55
 */
@RestController
@RequiredArgsConstructor
public class RmiNoticeFeignImpl implements RmiNoticeFeign {

    private final SysNoticeService noticeService;

    @Override
    public void sendNotice(SendNoticeReq req) {
        noticeService.sendUsers(
                req.getUserIds(),
                new SimpleMessageDTO(req.getTitle(), MessageConstants.NOTICE_TYPE_SYSTEM, req.getContent()));
    }

}
