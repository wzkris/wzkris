package com.wzkris.system.httpservice.notification;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.system.domain.dto.SimpleMessageDTO;
import com.wzkris.system.enums.NotificationTypeEnum;
import com.wzkris.system.httpservice.notification.req.NotificationReq;
import com.wzkris.system.service.NotificationInfoService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Hidden
@RestController
@RequiredArgsConstructor
public class NotificationInfoHttpServiceImpl implements NotificationInfoHttpService {

    private final NotificationInfoService notificationInfoService;

    @Override
    public void send2Users(NotificationReq req) {
        if (Objects.equals(req.getAuthType(), AuthTypeEnum.ADMIN)) {
            notificationInfoService.save2Admin(
                    req.getReceiverIds(),
                    new SimpleMessageDTO(req.getTitle(), NotificationTypeEnum.SYSTEM.getValue(), req.getContent()));
        } else if (Objects.equals(req.getAuthType(), AuthTypeEnum.TENANT)) {
            notificationInfoService.save2Tenant(
                    req.getReceiverIds(),
                    new SimpleMessageDTO(req.getTitle(), NotificationTypeEnum.SYSTEM.getValue(), req.getContent()));
        }
    }

}
