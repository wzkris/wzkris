package com.wzkris.system.impl.notification;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.system.api.notification.TenantNotificationInfoApi;
import com.wzkris.system.mapper.NotificationInfoMapper;
import com.wzkris.system.response.notification.NotificationInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantNotificationInfoApiImpl extends AbstractApi implements TenantNotificationInfoApi {

    private final NotificationInfoMapper notificationInfoMapper;

    @Override
    public Result<Page<NotificationInfoResponse>> queryPage(Boolean read, String notificationType) {
        startPage();
        List<NotificationInfoResponse> list = notificationInfoMapper.listTenantNotice(SecurityUtil.getUid(), notificationType, read);
        return getPageResult(list);
    }

    @Override
    public Result<Void> markRead(Long notificationId) {
        return toRes(notificationInfoMapper.markTenantRead(notificationId, SecurityUtil.getUid()));
    }

    @Override
    public Result<Integer> unreadSize(String notificationType) {
        int count = notificationInfoMapper.countTenantUnread(SecurityUtil.getUid(), notificationType);
        return ok(count);
    }

}
