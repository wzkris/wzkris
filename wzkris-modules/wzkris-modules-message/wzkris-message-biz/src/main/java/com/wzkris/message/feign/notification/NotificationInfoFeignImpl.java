package com.wzkris.message.feign.notification;

import com.wzkris.common.core.enums.AuthType;
import com.wzkris.message.constant.MessageConstants;
import com.wzkris.message.domain.dto.SimpleMessageDTO;
import com.wzkris.message.feign.notification.req.NotificationReq;
import com.wzkris.message.service.NotificationInfoService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Hidden
@RestController
@RequestMapping("/feign-notification")
@RequiredArgsConstructor
public class NotificationInfoFeignImpl implements NotificationInfoFeign {

    private final NotificationInfoService notificationInfoService;

    @Override
    public void send2Users(NotificationReq req) {
        if (Objects.equals(req.getAuthType(), AuthType.ADMIN)) {
            notificationInfoService.save2Admin(
                    req.getReceiverIds(),
                    new SimpleMessageDTO(req.getTitle(), MessageConstants.NOTIFICATION_TYPE_SYSTEM, req.getContent()));
        } else if (Objects.equals(req.getAuthType(), AuthType.TENANT)) {
            notificationInfoService.save2Tenant(
                    req.getReceiverIds(),
                    new SimpleMessageDTO(req.getTitle(), MessageConstants.NOTIFICATION_TYPE_SYSTEM, req.getContent()));
        }
    }

}
