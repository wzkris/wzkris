package com.wzkris.system.impl.notification;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.system.api.message.request.NotificationInfoPageRequest;
import com.wzkris.system.api.message.request.UnreadSizeQueryRequest;
import com.wzkris.system.api.notification.TenantNotificationInfoApi;
import com.wzkris.system.api.notification.response.NotificationInfoResponse;
import com.wzkris.system.mapper.NotificationInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantNotificationInfoApiImpl extends AbstractApi implements TenantNotificationInfoApi {

    private final NotificationInfoMapper notificationInfoMapper;

    @Override
    public Result<Page<NotificationInfoResponse>> queryPage(NotificationInfoPageRequest request) {
        startPage(request);
        List<NotificationInfoResponse> list = notificationInfoMapper.listTenantNotice(
                SecurityUtil.getUid(), request.getNotificationType(), request.getRead());
        return getPageResult(list);
    }

    @Override
    public Result<Void> markRead(IdRequest request) {
        return toRes(notificationInfoMapper.markTenantRead(request.getId(), SecurityUtil.getUid()));
    }

    @Override
    public Result<Integer> queryUnreadSize(UnreadSizeQueryRequest request) {
        int count = notificationInfoMapper.countTenantUnread(SecurityUtil.getUid(), request.getNotificationType());
        return ok(count);
    }

}
