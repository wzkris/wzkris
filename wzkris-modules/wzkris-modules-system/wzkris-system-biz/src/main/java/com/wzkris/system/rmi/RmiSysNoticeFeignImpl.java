package com.wzkris.system.rmi;

import com.wzkris.system.constant.MessageConstants;
import com.wzkris.system.domain.dto.SimpleMessageDTO;
import com.wzkris.system.rmi.domain.req.SendNoticeReq;
import com.wzkris.system.service.SysNoticeService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class RmiSysNoticeFeignImpl implements RmiSysNoticeFeign {

    private final SysNoticeService noticeService;

    @Override
    public void send2Users(SendNoticeReq req) {
        noticeService.saveBatch2Users(
                req.getUserIds(),
                new SimpleMessageDTO(req.getTitle(), MessageConstants.NOTICE_TYPE_SYSTEM, req.getContent()));
    }

}
