package com.wzkris.system.rmi;

import com.wzkris.system.constant.MessageConstants;
import com.wzkris.system.domain.dto.SimpleMessageDTO;
import com.wzkris.system.rmi.domain.req.NotificationReq;
import com.wzkris.system.service.NotificationInfoService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/feign-notification")
@RequiredArgsConstructor
public class NotificationInfoFeignImpl implements NotificationInfoFeign {

    private final NotificationInfoService notificationInfoService;

    @Override
    public void send2Users(NotificationReq req) {
        notificationInfoService.saveBatchAndNotify(
                req.getUserIds(),
                new SimpleMessageDTO(req.getTitle(), MessageConstants.NOTICE_TYPE_SYSTEM, req.getContent()));
    }

}
