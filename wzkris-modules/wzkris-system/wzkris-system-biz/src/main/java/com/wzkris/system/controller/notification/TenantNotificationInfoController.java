package com.wzkris.system.controller.notification;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.api.notification.TenantNotificationInfoApi;
import com.wzkris.system.response.notification.NotificationInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "租户通知信息")
@RestController
@RequestMapping("/notification-infot")
@RequiredArgsConstructor
public class TenantNotificationInfoController {

    private final TenantNotificationInfoApi tenantNotificationInfoApi;

    @Operation(summary = "通知分页")
    @GetMapping("/query-page")
    public Result<Page<NotificationInfoResponse>> queryPage(Boolean read, String notificationType) {
        return tenantNotificationInfoApi.queryPage(read, notificationType);
    }

    @Operation(summary = "标记已读")
    @PostMapping("/mark-read")
    public Result<Void> markRead(@RequestBody Long notificationId) {
        return tenantNotificationInfoApi.markRead(notificationId);
    }

    @Operation(summary = "未读数量")
    @GetMapping("/query-unread-size")
    public Result<Integer> unreadSize(String notificationType) {
        return tenantNotificationInfoApi.unreadSize(notificationType);
    }

}

