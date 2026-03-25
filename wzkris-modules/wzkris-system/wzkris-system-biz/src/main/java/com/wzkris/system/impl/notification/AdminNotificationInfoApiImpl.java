package com.wzkris.system.impl.notification;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.system.api.notification.AdminNotificationInfoApi;
import com.wzkris.system.mapper.NotificationInfoMapper;
import com.wzkris.system.response.notification.NotificationInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminNotificationInfoApiImpl extends BaseController implements AdminNotificationInfoApi {

    private final NotificationInfoMapper notificationInfoMapper;

    @Override
    public Result<Page<NotificationInfoResponse>> queryPage(String read, String notificationType) {
        startPage();
        List<NotificationInfoResponse> list = notificationInfoMapper.listAdminNotice(SecurityUtil.getUid(), notificationType, read);
        return getDataTable(list);
    }

    @Override
    public Result<Void> markRead(Long notificationId) {
        return toRes(notificationInfoMapper.markAdminRead(notificationId, SecurityUtil.getUid()));
    }

    @Override
    public Result<Integer> unreadSize(String notificationType) {
        int count = notificationInfoMapper.countAdminUnread(SecurityUtil.getUid(), notificationType);
        return ok(count);
    }

}
