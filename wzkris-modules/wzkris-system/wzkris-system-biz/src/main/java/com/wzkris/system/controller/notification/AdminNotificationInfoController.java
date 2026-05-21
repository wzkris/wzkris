package com.wzkris.system.controller.notification;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.system.api.notification.AdminNotificationInfoApi;
import com.wzkris.system.api.notification.request.NotificationInfoPageRequest;
import com.wzkris.system.api.notification.request.UnreadSizeQueryRequest;
import com.wzkris.system.api.notification.response.NotificationInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理员通知信息")
@RestController
@RequestMapping("/notification-info")
@RequiredArgsConstructor
public class AdminNotificationInfoController {

    private final AdminNotificationInfoApi adminNotificationInfoApi;

    @Operation(summary = "通知分页")
    @GetMapping("/query-page")
    public Result<Page<NotificationInfoResponse>> queryPage(@ParameterObject NotificationInfoPageRequest request) {
        return adminNotificationInfoApi.queryPage(request);
    }

    @Operation(summary = "标记已读")
    @PostMapping("/mark-read")
    public Result<Void> markRead(@RequestBody @Valid IdRequest request) {
        return adminNotificationInfoApi.markRead(request);
    }

    @Operation(summary = "未读数量")
    @GetMapping("/query-unread-size")
    public Result<Integer> queryUnreadSize(@ParameterObject UnreadSizeQueryRequest request) {
        return adminNotificationInfoApi.queryUnreadSize(request);
    }

}

