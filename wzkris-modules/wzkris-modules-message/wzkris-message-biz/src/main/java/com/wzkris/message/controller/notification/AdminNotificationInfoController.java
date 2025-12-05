package com.wzkris.message.controller.notification;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.AdminUtil;
import com.wzkris.message.domain.vo.notification.NotificationInfoVO;
import com.wzkris.message.mapper.NotificationInfoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "管理员通知信息")
@RestController
@RequestMapping("/notification-info")
@RequiredArgsConstructor
public class AdminNotificationInfoController extends BaseController {

    private final NotificationInfoMapper notificationInfoMapper;

    @Operation(summary = "通知分页")
    @GetMapping("/list")
    public Result<Page<NotificationInfoVO>> list(String read, String notificationType) {
        startPage();
        List<NotificationInfoVO> list = notificationInfoMapper.listAdminNotice(AdminUtil.getId(), notificationType, read);
        return getDataTable(list);
    }

    @Operation(summary = "标记已读")
    @PostMapping("/mark-read")
    public Result<Void> markRead(@RequestBody Long notificationId) {
        return toRes(notificationInfoMapper.markAdminRead(notificationId, AdminUtil.getId()));
    }

    @Operation(summary = "未读数量")
    @GetMapping("/unread-size")
    public Result<Integer> unreadSize(String notificationType) {
        int count = notificationInfoMapper.countAdminUnread(AdminUtil.getId(), notificationType);
        return ok(count);
    }

}
