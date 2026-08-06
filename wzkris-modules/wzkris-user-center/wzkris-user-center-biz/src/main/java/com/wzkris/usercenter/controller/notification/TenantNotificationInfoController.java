package com.wzkris.usercenter.controller.notification;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.notification.TenantNotificationInfoApi;
import com.wzkris.usercenter.api.notification.request.NotificationInfoPageRequest;
import com.wzkris.usercenter.api.notification.request.UnreadSizeQueryRequest;
import com.wzkris.usercenter.api.notification.response.NotificationInfoPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@Tag(name = "租户通知信息")
@RestController
@RequestMapping("/notification-infot")
@RequiredArgsConstructor
public class TenantNotificationInfoController {

    private final TenantNotificationInfoApi tenantNotificationInfoApi;

    @Operation(summary = "通知分页")
    @GetMapping("/query-page")
    public Result<Page<NotificationInfoPageResponse>> queryPage(@ParameterObject NotificationInfoPageRequest request) {
        return tenantNotificationInfoApi.queryPage(request);
    }

    @Operation(summary = "标记已读")
    @PostMapping("/mark-read")
    public Result<Void> markRead(@RequestBody @Valid IdRequest request) {
        return tenantNotificationInfoApi.markRead(request);
    }

    @Operation(summary = "未读数量")
    @GetMapping("/query-unread-size")
    public Result<Integer> queryUnreadSize(@ParameterObject UnreadSizeQueryRequest request) {
        return tenantNotificationInfoApi.queryUnreadSize(request);
    }

}

