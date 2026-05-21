package com.wzkris.system.impl.notification;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.system.api.notification.AdminNotificationInfoApi;
import com.wzkris.system.api.notification.request.NotificationInfoPageRequest;
import com.wzkris.system.api.notification.request.UnreadSizeQueryRequest;
import com.wzkris.system.api.notification.response.NotificationInfoResponse;
import com.wzkris.system.mapper.NotificationInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminNotificationInfoApiImpl extends AbstractApi implements AdminNotificationInfoApi {

    private final NotificationInfoMapper notificationInfoMapper;

    @Override
    public Result<Page<NotificationInfoResponse>> queryPage(NotificationInfoPageRequest request) {
        startPage(request);
        List<NotificationInfoResponse> list = notificationInfoMapper.listAdminNotice(
                SecurityUtil.getUid(), request.getNotificationType(), request.getRead());
        return getPageResult(list);
    }

    @Override
    public Result<Void> markRead(IdRequest request) {
        return toRes(notificationInfoMapper.markAdminRead(request.getId(), SecurityUtil.getUid()));
    }

    @Override
    public Result<Integer> queryUnreadSize(UnreadSizeQueryRequest request) {
        int count = notificationInfoMapper.countAdminUnread(SecurityUtil.getUid(), request.getNotificationType());
        return ok(count);
    }

}
