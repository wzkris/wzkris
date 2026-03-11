package com.wzkris.system.httpclient.notification;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.system.domain.dto.SimpleMessageDTO;
import com.wzkris.system.enums.NotificationTypeEnum;
import com.wzkris.system.httpclient.notification.req.NotificationReq;
import com.wzkris.system.service.NotificationInfoService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Hidden
@RestController
@RequiredArgsConstructor
public class NotificationInfoClientImpl implements NotificationInfoClient {

    private final NotificationInfoService notificationInfoService;

    @Override
    public Result<Void> send2Users(NotificationReq req) {
        if (Objects.equals(req.getAuthType(), AuthTypeEnum.ADMIN)) {
            notificationInfoService.save2Admin(
                    req.getReceiverIds(),
                    new SimpleMessageDTO(req.getTitle(), NotificationTypeEnum.SYSTEM.getValue(), req.getContent()));
        } else if (Objects.equals(req.getAuthType(), AuthTypeEnum.TENANT)) {
            notificationInfoService.save2Tenant(
                    req.getReceiverIds(),
                    new SimpleMessageDTO(req.getTitle(), NotificationTypeEnum.SYSTEM.getValue(), req.getContent()));
        }
        return Result.ok();
    }

}
